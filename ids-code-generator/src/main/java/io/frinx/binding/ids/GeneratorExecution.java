/*
 * Copyright © 2017 Frinx and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package io.frinx.binding.ids;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.opendaylight.mdsal.binding.generator.impl.BindingGeneratorImpl;
import org.opendaylight.mdsal.binding.model.api.Type;
import org.opendaylight.mdsal.binding.model.util.BindingGeneratorUtil;
import org.opendaylight.yangtools.yang.binding.BindingMapping;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.model.api.DataNodeContainer;
import org.opendaylight.yangtools.yang.model.api.DataSchemaNode;
import org.opendaylight.yangtools.yang.model.api.GroupingDefinition;
import org.opendaylight.yangtools.yang.model.api.Module;
import org.opendaylight.yangtools.yang.model.api.SchemaContext;
import org.opendaylight.yangtools.yang.model.api.SchemaNode;
import org.opendaylight.yangtools.yang.model.api.SchemaPath;
import org.opendaylight.yangtools.yang.model.api.UsesNode;

final class GeneratorExecution implements AutoCloseable {

    private final SchemaContext context;
    private final VariableNameCache varCache;
    private final IdsClassTemplate template;
    private final List<Type> types;
    private final Set<URI> currentNamespaces;

    GeneratorExecution(SchemaContext context, VariableNameCache varCache, Set<Module> currentModules, String packageName) {
        this.context = context;
        this.varCache = varCache;
        this.template = new IdsClassTemplate(packageName);

        this.types = new BindingGeneratorImpl().generateTypes(context);
        this.currentNamespaces = currentModules.stream().map(a -> a.getQNameModule().getNamespace()).collect(Collectors.toSet());
    }

    @Override
    public void close() {

    }

    Set<File> execute(File outputBaseDir) throws IOException {
        context.getModules().forEach(this::executeModule);
        return writeFile(outputBaseDir);
    }

    private void executeModule(Module module) {
        Collection<DataSchemaNode> childNodes = module.getChildNodes();
//        Set<AugmentationSchema> augNodes = module.getAugmentations();

        template.addModuleStart(module);
        processChildNodes(module, childNodes, Collections.emptyList(), Collections.emptyList());

        // TODO augmentations cannot be easily generated, there is a no trivial way for how to find
        // augmentationNode -> Type mapping and without knowing the augmentation Type we cannot generate the ID
        // TODO find the mapping between aug(SchemaNode) <-> Type and generate the IDs
        // Augment ID is needed here !

//        for (AugmentationSchema augNode : augNodes) {
//            Collection<DataSchemaNode> augChildren = augNode.getChildNodes();
//
//            SchemaPath targetPath = augNode.getTargetPath();
//            DataSchemaNode targetNode = (DataSchemaNode) SchemaContextUtil.findNodeInSchemaContext(context, targetPath.getPathFromRoot());
//            String fqn = getFqn(rootPackage, targetNode, targetPath);
//            Optional<Type> targetType = findType(types, fqn);
//
//            //    ArrayList<QName> schemaPathArgs = Lists.newArrayList(augNode.getTargetPath().getPathFromRoot());
//            processChildNodes(augNode, rootPackage, augChildren, Collections.emptyList(), Collections.emptyList());
//        }
    }

//    private Optional<Type> getAugmentTargetType(Type type) {
//        if (type instanceof GeneratedType) {
//            for (Type implementedType : ((GeneratedType) type).getImplements()) {
//                if (implementedType instanceof ParameterizedType &&
//                    ((ParameterizedType) implementedType).getRawType().getFullyQualifiedName().equals(Augmentation.class.getName())) {
//                    return Optional.of(((ParameterizedType) implementedType).getActualTypeArguments()[0]);
//                }
//            }
//        }
//
//        return Optional.empty();
//    }

    private Set<File> writeFile(File outputBaseDir) throws IOException {
        File outputFile = new File(outputBaseDir, IdsClassTemplate.CLS_NAME + ".java");
        try {
            Files.createParentDirs(outputFile);
            Files.touch(outputFile);
            Files.write(template.getContent(), outputFile, Charsets.UTF_8);
        } catch (IOException e) {
            throw new IOException("Unable to generate IDs class at: " + outputFile, e);
        }

        return Collections.singleton(outputFile);
    }

    private void processChildNodes(final DataNodeContainer parent,
                                   final Collection<DataSchemaNode> childNodes,
                                   final List<QName> parentTypePathArgs,
                                   final List<QName> parentSchemaPathArgs) {
        childNodes.stream()
                .filter(this::isComplexNode)
                .filter(child -> isInCurrentYang(currentNamespaces, child))
                .sorted(Comparator.comparing(SchemaNode::getQName))
                .forEach(child -> processChild(parent, parentTypePathArgs, parentSchemaPathArgs, child));
    }

    private void processChild(DataNodeContainer parent, List<QName> parentTypePathArgs, List<QName> parentSchemaPathArgs, DataSchemaNode dataSchemaNode) {

        // create a copy of schema path args, since we are using a mutable list here
        List<QName> typePathArgs = new ArrayList<>(parentTypePathArgs);
        List<QName> schemaPathArgs = new ArrayList<>(parentSchemaPathArgs);

        // Get name of parent variable if any
        Optional<String> parentVarName = schemaPathArgs.isEmpty() ?
                Optional.empty() :
                varCache.getExistingVariableName(SchemaPath.create(schemaPathArgs, true));

        fixTypePathForGroupings(parent, dataSchemaNode, typePathArgs);
        typePathArgs.add(dataSchemaNode.getQName());
        schemaPathArgs.add(dataSchemaNode.getQName());

        SchemaPath typePath = SchemaPath.create(typePathArgs, true);
        SchemaPath schemaPath = SchemaPath.create(schemaPathArgs, true);

        String varName = varCache.getVariableName(schemaPath);
        String fqn = getFqn(dataSchemaNode, typePath);

        // If the type was not found we might be facing augmentation or there's a problem with package name
        // either way, skip and cut the recursion subtree
        if (findType(types, fqn).isPresent()) {
            template.addId(parentVarName, dataSchemaNode, schemaPath, varName, fqn);
            Collection<DataSchemaNode> children = ((DataNodeContainer) dataSchemaNode).getChildNodes();
            processChildNodes(((DataNodeContainer) dataSchemaNode), children, typePathArgs, schemaPathArgs);
        }
    }

    private String getFqn(DataSchemaNode dataSchemaNode, SchemaPath typePath) {
        String className = BindingMapping.getClassName(dataSchemaNode.getQName());
        String rootPackageName = BindingMapping.getRootPackageName(typePath.getPathFromRoot().iterator().next());
        String packageName = BindingGeneratorUtil.packageNameForGeneratedType(rootPackageName, typePath);
        return packageName + "." + className;
    }

    private Optional<Type> findType(List<Type> types, String fqn) {
        return types.stream()
                .filter(a -> a.getFullyQualifiedName().equals(fqn))
                .findFirst();
    }

    private boolean isComplexNode(DataSchemaNode dataSchemaNode) {
        return dataSchemaNode instanceof DataNodeContainer;
    }

    private boolean isInCurrentYang(Set<URI> currentNamespaces, DataSchemaNode dataSchemaNode) {
        return currentNamespaces.contains(dataSchemaNode.getQName().getNamespace());
    }

    /**
     * Check if type is from grouping and if so, fix the type path by replacing the usage path with grouping definition path
     * Since types for groupings are generated at their definition place rather than place of use
     */
    private void fixTypePathForGroupings(DataNodeContainer parent,
                                         DataSchemaNode dataSchemaNode,
                                         List<QName> typePathArgs) {
        if (dataSchemaNode.isAddedByUses()) {
            Set<UsesNode> uses = parent.getUses();

            if (uses.size() == 0) {
                // Not direct child of grouping, no type fix necessary
            } else {
                for (UsesNode use : uses) {
                    GroupingDefinition groupingDefinition = findGroupingDefinition(use);
                    Optional<DataSchemaNode> foundChild = groupingDefinition.getChildNodes().stream()
                            .filter(a -> a.getQName().getLocalName().equals(dataSchemaNode.getQName().getLocalName()))
                            .findFirst();
                    if (foundChild.isPresent()) {
                        typePathArgs.clear();
                        typePathArgs.add(use.getGroupingPath().getLastComponent());
                        // Since there can be uses in grouping, check the path recursively
                        fixTypePathForGroupings(groupingDefinition, foundChild.get(), typePathArgs);
                        break;
                    }
                }
            }
        }
    }

    private GroupingDefinition findGroupingDefinition(UsesNode use) {
        return context.getGroupings().stream()
                .filter(a -> a.getQName().equals(use.getGroupingPath().getLastComponent()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unable to find grouping definition for " + use));
    }
}
