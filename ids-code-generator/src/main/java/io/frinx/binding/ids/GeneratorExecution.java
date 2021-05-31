/*
 * Copyright © 2020 Frinx and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.frinx.binding.ids;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import io.frinx.binding.ids.BindingLookup.GeneratedTypeMeta;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.opendaylight.mdsal.binding.generator.impl.BindingGeneratorImpl;
import org.opendaylight.mdsal.binding.model.api.GeneratedType;
import org.opendaylight.mdsal.binding.model.api.ParameterizedType;
import org.opendaylight.mdsal.binding.model.api.Type;
import org.opendaylight.yangtools.yang.binding.BindingMapping;
import org.opendaylight.yangtools.yang.binding.ChildOf;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.model.api.AugmentationSchemaNode;
import org.opendaylight.yangtools.yang.model.api.DataNodeContainer;
import org.opendaylight.yangtools.yang.model.api.DataSchemaNode;
import org.opendaylight.yangtools.yang.model.api.Module;
import org.opendaylight.yangtools.yang.model.api.SchemaContext;
import org.opendaylight.yangtools.yang.model.api.SchemaNode;
import org.opendaylight.yangtools.yang.model.api.SchemaPath;

final class GeneratorExecution implements AutoCloseable {

    private final SchemaContext context;
    private final VariableNameCache varCache;
    private final IdsClassTemplate template;
    private final List<Type> types;
    private final Set<URI> currentNamespaces;

    GeneratorExecution(SchemaContext context, VariableNameCache varCache, Set<Module> currentModules, String
            packageName) {
        this.context = context;
        this.varCache = varCache;
        this.template = new IdsClassTemplate(packageName);

        this.types = new BindingGeneratorImpl().generateTypes(context);
        // Sort types to make sure order is always predictable
        types.sort(Comparator.comparing(Type::getFullyQualifiedName));
        this.currentNamespaces = currentModules.stream()
                .map(a -> a.getQNameModule()
                        .getNamespace())
                .collect(Collectors.toSet());
    }

    @Override
    public void close() {

    }

    Set<File> execute(File outputBaseDir) throws IOException {
        context.getModules()
                .stream()
                .sorted(Comparator.comparing(m -> m.getQNameModule().toString()))
                .forEach(this::executeModule);

        context.getModules()
                .stream()
                .sorted(Comparator.comparing(m -> m.getQNameModule().toString()))
                .forEach(this::executeModuleAug);

        return writeFile(outputBaseDir);
    }

    private void executeModule(Module module) {
        Collection<DataSchemaNode> childNodes = module.getChildNodes();
        template.addModuleStart(module, BindingMapping.getRootPackageName(module.getQNameModule()));

        processChildNodes(module, Optional.empty(), childNodes, Collections.emptyList(), Collections.emptyList());
    }

    private void executeModuleAug(Module module) {
        Set<AugmentationSchemaNode> augNodes = module.getAugmentations();
        template.addModuleAugStart(module);
        processAugmentations(augNodes, module);
    }

    private void processAugmentations(Set<AugmentationSchemaNode> augNodes, Module module) {
        for (AugmentationSchemaNode augNode : BindingLookup.sortAugNodes(augNodes)) {

            if (!currentNamespaces.contains(module.getQNameModule().getNamespace())) {
                // Skip dependency modules, only generate local
                continue;
            }

            processAugmentation(module, augNode);
        }
    }

    private void processAugmentation(Module module, AugmentationSchemaNode augNode) {
        List<GeneratedTypeMeta> currentAugMetas = BindingLookup.extractAugMeta(augNode, context, types);
        GeneratedTypeMeta currentAugMeta = Iterables.getLast(currentAugMetas);

        List<Type> augTypes = BindingLookup.findAllAugTypes(types, augNode, currentAugMeta.targetType, module);

        for (Type augType : augTypes) {

            AbstractMap.SimpleEntry<SchemaPath, LinkedHashMap<String, String>> parentPathMeta =
                    BindingLookup.constructParentTypePaths(currentAugMetas, context, types);

            String varName = varCache.getAugmentationVariableName(currentAugMeta.originalTargetPath,
                    augType, module.getQNameModule());
            template.addAugId(varName,
                    augType.getFullyQualifiedName(),
                    parentPathMeta.getValue(),
                    parentPathMeta.getKey());

            SchemaPath augTypeSchemaPath = VariableNameCache.getAugmentPath(currentAugMeta.targetPathWithGroupings,
                    augType, module.getQNameModule());
            SchemaPath augSchemaPath = VariableNameCache.getAugmentPath(currentAugMeta.originalTargetPath,
                    augType, module.getQNameModule());

            processChildNodes(augNode, Optional.of(augType),
                    augNode.getChildNodes(),
                    Lists.newArrayList(augTypeSchemaPath.getPathFromRoot()),
                    Lists.newArrayList(augSchemaPath.getPathFromRoot()));
        }
    }

    private Set<File> writeFile(File outputBaseDir) throws IOException {
        File outputFile = new File(outputBaseDir, IdsClassTemplate.CLS_NAME + ".java");
        try {
            Files.createParentDirs(outputFile);
            Files.touch(outputFile);
            Files.write(template.getContent(), outputFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IOException("Unable to generate IDs class at: " + outputFile, e);
        }

        return Collections.singleton(outputFile);
    }

    private void processChildNodes(final DataNodeContainer parent,
                                   final Optional<Type> parentType,
                                   final Collection<DataSchemaNode> childNodes,
                                   final List<QName> parentTypePathArgs,
                                   final List<QName> parentSchemaPathArgs) {
        childNodes.stream()
                .filter(this::isComplexNode)
                .filter(child -> isInCurrentYang(currentNamespaces, child))
                .sorted(Comparator.comparing(SchemaNode::getQName))
                .forEach(child -> processChild(parent, parentType, parentTypePathArgs, parentSchemaPathArgs, child));
    }

    private void processChild(DataNodeContainer parent,
                              Optional<Type> parentType,
                              List<QName> parentTypePathArgs,
                              List<QName> parentSchemaPathArgs,
                              DataSchemaNode dataSchemaNode) {

        // create a copy of schema path args, since we are using a mutable list here
        List<QName> typePathArgs = new ArrayList<>(parentTypePathArgs);
        List<QName> schemaPathArgs = new ArrayList<>(parentSchemaPathArgs);

        // Get name of parent variable if any
        Optional<String> parentVarName = schemaPathArgs.isEmpty()
                ? Optional.empty()
                : varCache.getExistingVariableName(SchemaPath.create(schemaPathArgs, true));

        BindingLookup.fixTypePathForGroupings(parent, dataSchemaNode, typePathArgs, context);
        typePathArgs.add(dataSchemaNode.getQName());
        schemaPathArgs.add(dataSchemaNode.getQName());

        SchemaPath typePath = SchemaPath.create(typePathArgs, true);
        SchemaPath schemaPath = SchemaPath.create(schemaPathArgs, true);

        String varName = varCache.getVariableName(schemaPath);
        String fqn = BindingLookup.getFqnFromParent(dataSchemaNode, typePath);

        // If the type was not found we might be facing augmentation or there's a problem with package name
        // either way, skip and cut the recursion subtree
        Optional<Type> type = BindingLookup.findType(parentType, dataSchemaNode, typePath, fqn, types);
        if (!type.isPresent()) {
            type = BindingLookup.findType(parentType, dataSchemaNode, schemaPath, fqn, types);
        }

        if (type.isPresent()) {
            template.addId(parentVarName, dataSchemaNode, schemaPath, varName, type.get().getFullyQualifiedName());
            Collection<DataSchemaNode> children = ((DataNodeContainer) dataSchemaNode).getChildNodes();
            processChildNodes(((DataNodeContainer) dataSchemaNode), type, children, typePathArgs, schemaPathArgs);
        }
    }

    private boolean isParentChild(Type parent, Type child) {
        return ((GeneratedType) child).getImplements().stream()
                .filter(implType -> implType instanceof ParameterizedType)
                .filter(implType -> implType.getFullyQualifiedName().equals(ChildOf.class.getName()))
                .findFirst()
                .map(childOfType -> ((ParameterizedType) childOfType).getActualTypeArguments()[0])
                .map(childOfParameter -> childOfParameter.getFullyQualifiedName()
                        .equals(parent.getFullyQualifiedName()))
                .orElse(false);
    }

    private boolean isComplexNode(DataSchemaNode dataSchemaNode) {
        return dataSchemaNode instanceof DataNodeContainer;
    }

    private boolean isInCurrentYang(Set<URI> uriSet, DataSchemaNode dataSchemaNode) {
        return uriSet.contains(dataSchemaNode.getQName()
                .getNamespace());
    }

}
