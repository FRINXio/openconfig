/*
 * Copyright © 2017 Frinx and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package io.frinx.binding.ids;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.opendaylight.mdsal.binding.model.api.GeneratedType;
import org.opendaylight.mdsal.binding.model.api.MethodSignature;
import org.opendaylight.mdsal.binding.model.api.ParameterizedType;
import org.opendaylight.mdsal.binding.model.api.Type;
import org.opendaylight.mdsal.binding.model.api.TypeMember;
import org.opendaylight.mdsal.binding.model.util.BindingGeneratorUtil;
import org.opendaylight.mdsal.binding.model.util.ReferencedTypeImpl;
import org.opendaylight.yangtools.yang.binding.Augmentation;
import org.opendaylight.yangtools.yang.binding.BindingMapping;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.model.api.AugmentationSchemaNode;
import org.opendaylight.yangtools.yang.model.api.AugmentationTarget;
import org.opendaylight.yangtools.yang.model.api.DataNodeContainer;
import org.opendaylight.yangtools.yang.model.api.DataSchemaNode;
import org.opendaylight.yangtools.yang.model.api.GroupingDefinition;
import org.opendaylight.yangtools.yang.model.api.Module;
import org.opendaylight.yangtools.yang.model.api.SchemaContext;
import org.opendaylight.yangtools.yang.model.api.SchemaNode;
import org.opendaylight.yangtools.yang.model.api.SchemaPath;
import org.opendaylight.yangtools.yang.model.api.UsesNode;
import org.opendaylight.yangtools.yang.model.util.SchemaContextUtil;

final class BindingLookup {

    private BindingLookup() {}

    /**
     * Match get, or is prefix for BA getters.
     */
    private static final String GETTER_PREFIX = "[a-z]+";

    static Module findModuleByQname(QName qname, SchemaContext context) {
        return context.findModule(qname.getNamespace(), qname.getRevision()).get();
    }

    static SchemaNode findNodeInSchemaCtx(SchemaPath currentPath, SchemaContext context) {
        SchemaNode node = SchemaContextUtil.findNodeInSchemaContext(context, currentPath.getPathFromRoot());
        if (node != null) {
            return node;
        }

        // Fallback when searching for grouping node
        for (QName qn : currentPath.getPathTowardsRoot()) {
            SchemaPath currentElementPath = SchemaPath.create(true, qn);
            node = SchemaContextUtil.findDataSchemaNode(context, currentElementPath);
            if (node != null) {
                break;
            }
        }

        // Fallback when searching for grouping node children
        SchemaPath relativePath = SchemaPath.create(false);
        for (QName qn : currentPath.getPathTowardsRoot()) {
            SchemaPath currentElementPath = SchemaPath.create(false, qn);
            relativePath = SchemaPath.create(
                    Iterables.concat(currentElementPath.getPathFromRoot(), relativePath.getPathFromRoot()), false);
            node = SchemaContextUtil.findDataSchemaNode(context, relativePath);
            if (node != null) {
                break;
            }
        }

        // Fallback when searching for grouping node children used in different modules (namespace)
        relativePath = SchemaPath.create(false);
        for (QName qn : currentPath.getPathTowardsRoot()) {
            SchemaPath currentElementPath = SchemaPath.create(false, qn);
            relativePath = SchemaPath.create(
                    Iterables.concat(currentElementPath.getPathFromRoot(),
                            StreamSupport.stream(relativePath.getPathFromRoot().spliterator(), false)
                                    .map(q -> QName.create(qn, q.getLocalName()))
                                    .collect(Collectors.toList())),
                    false);
            node = SchemaContextUtil.findDataSchemaNode(context, relativePath);
            if (node != null) {
                break;
            }
        }

        return node;
    }

    static List<AugmentationSchemaNode> sortAugNodes(Set<AugmentationSchemaNode> augNodes) {
        return augNodes.stream()
                .sorted(Comparator.comparing(BindingLookup::augToString))
                .collect(Collectors.toList());
    }

    /**
     * Get a unique string for an augmentation.
     */
    private static String augToString(AugmentationSchemaNode aug) {
        StringBuffer buf = new StringBuffer();
        buf.append(aug.getTargetPath().toString());
        aug.getChildNodes().forEach(c -> buf.append(c.getQName().toString()));
        aug.getUses().forEach(c -> buf.append(c.getGroupingPath().toString()));
        return buf.toString();
    }

    static Optional<Type> findType(Optional<Type> parentType,
                                   DataSchemaNode targetNode,
                                   SchemaPath fixedTargetPath,
                                   String fqn,
                                   List<Type> types) {
        Optional<Type> targetType = findType(types, fqn);

        if (targetType.isPresent()) {
            return targetType;
        }

        // Look up type based on getter in parent if possible
        if (parentType.isPresent() && (parentType.get() instanceof GeneratedType)) {
            return collectMethodsMeta((GeneratedType) parentType.get())
                    .stream()
                    .filter(m -> m.getName().replaceFirst(GETTER_PREFIX, "")
                            .equals(BindingMapping.getGetterSuffix(targetNode.getQName())))
                    .findFirst()
                    .map(TypeMember::getReturnType);
        }

        // Provided FQN does not have to be correct, try different FQN as a fallback
        // Usually when the type comes from grouping the fqn might be different than expected
        SchemaPath currentPath = SchemaPath.create(false);
        for (QName qname : fixedTargetPath.getPathTowardsRoot()) {
            SchemaPath currentElementPath = SchemaPath.create(false, qname);
            currentPath = SchemaPath.create(
                    Iterables.concat(currentElementPath.getPathFromRoot(), currentPath.getPathFromRoot()), false);

            String fallbackFqn = getFqn(targetNode, currentPath);
            Optional<Type> fallbackTargetType = findType(types, fallbackFqn);

            if (fallbackTargetType.isPresent()) {
                return fallbackTargetType;
            }
        }

        // Another fallback, covers cases where groupings are using groupings from another namespace
        currentPath = SchemaPath.create(false);
        for (QName qname : fixedTargetPath.getPathTowardsRoot()) {
            SchemaPath currentElementPath = SchemaPath.create(false, qname);
            currentPath = SchemaPath.create(
                    Iterables.concat(currentElementPath.getPathFromRoot(), currentPath.getPathFromRoot()), false);

            String fallbackFqn = getFqnFromParent(targetNode, currentPath);
            Optional<Type> fallbackTargetType = findType(types, fallbackFqn);

            if (fallbackTargetType.isPresent()) {
                return fallbackTargetType;
            }
        }

        // Another fallback, if augmentations are involved
        boolean containsAugmentations =
                StreamSupport.stream(fixedTargetPath.getPathFromRoot().spliterator(), false)
                        .anyMatch(qName -> qName.getLocalName().startsWith(VariableNameCache.AUG_PREFIX));
        if (containsAugmentations) {
            currentPath = SchemaPath.create(StreamSupport.stream(fixedTargetPath.getPathFromRoot().spliterator(), false)
                    .filter(qName -> !qName.getLocalName().startsWith(VariableNameCache.AUG_PREFIX))
                    .collect(Collectors.toList()), true);
            return findType(parentType, targetNode, currentPath, fqn, types);
        }

        return Optional.empty();
    }

    static Optional<Type> findType(List<Type> types, String fqn) {
        return types.stream()
                .filter(a -> a.getFullyQualifiedName().equals(fqn))
                .findFirst();
    }

    static String getFqn(DataSchemaNode dataSchemaNode, SchemaPath typePath) {
        String className = BindingMapping.getClassName(dataSchemaNode.getQName());
        String rootPackageName = BindingMapping.getRootPackageName(typePath.getLastComponent());

        String packageName = BindingGeneratorUtil.packageNameForGeneratedType(rootPackageName, typePath);
        return packageName + "." + className;
    }

    static String getFqnFromParent(DataSchemaNode dataSchemaNode, SchemaPath typePath) {
        String className = BindingMapping.getClassName(dataSchemaNode.getQName());
        String rootPackageName = BindingMapping.getRootPackageName(typePath.getPathFromRoot().iterator().next());

        String packageName = BindingGeneratorUtil.packageNameForGeneratedType(rootPackageName, typePath);
        return packageName + "." + className;
    }

    /**
     * Find generated type for an augmentation based on child nodes added by the augmentation.
     */
    static Type findAugType(List<Type> types,
                            AugmentationSchemaNode fqn,
                            Type type,
                            Module module) {
        return findAllAugTypes(types, fqn, type, module).stream().findFirst().get();
    }

    static List<Type> findAllAugTypes(List<Type> types,
                                      AugmentationSchemaNode fqn,
                                      Type type,
                                      Module module) {
        String augPackage = BindingMapping.getRootPackageName(module.getQNameModule());

        Set<String> getters = fqn.getChildNodes().stream()
                .map(c -> BindingMapping.getGetterSuffix(c.getQName()))
                .collect(Collectors.toSet());

        return types.stream()
                .filter(a -> a.getPackageName().equals(augPackage))
                .filter(a -> a instanceof GeneratedType)
                .filter(a -> implementsAugmentationTypes(type, (GeneratedType) a))
                .filter(a -> collectMethods((GeneratedType) a).containsAll(getters))
                .collect(Collectors.toList());
    }

    /**
     * Recursively collect all methods from a generated type (without "get" or "is" prefixes).
     */
    private static Set<String> collectMethods(GeneratedType type) {
        Set<String> methods = type.getImplements()
                .stream()
                .filter(t -> t instanceof GeneratedType)
                .flatMap(t -> ((GeneratedType) t).getMethodDefinitions().stream().map(TypeMember::getName))
                // Remove "get" or "is" prefix
                .map(m -> m.replaceFirst(GETTER_PREFIX, ""))
                .collect(Collectors.toSet());

        methods.addAll(type.getMethodDefinitions()
                .stream()
                .map(TypeMember::getName)
                // Remove "get" or "is" prefix
                .map(m -> m.replaceFirst(GETTER_PREFIX, ""))
                .collect(Collectors.toSet()));

        type.getImplements().stream()
                .filter(a -> a instanceof GeneratedType)
                .flatMap(nT -> collectMethods(((GeneratedType) nT)).stream())
                .forEach(methods::add);

        return methods;
    }

    /**
     * Recursively collect all methods from a generated type (without "get" or "is" prefixes).
     */
    private static Set<MethodSignature> collectMethodsMeta(GeneratedType type) {
        Set<MethodSignature> methods = type.getImplements()
                .stream()
                .filter(t -> t instanceof GeneratedType)
                .flatMap(t -> ((GeneratedType) t).getMethodDefinitions().stream())
                .collect(Collectors.toSet());

        methods.addAll(new HashSet<>(type.getMethodDefinitions()));

        type.getImplements().stream()
                .filter(a -> a instanceof GeneratedType)
                .flatMap(nT -> collectMethodsMeta(((GeneratedType) nT)).stream())
                .forEach(methods::add);

        return methods;
    }

    /**
     * Find generated type that is an augmentation to provided target type.
     */
    private static boolean implementsAugmentationTypes(Type augTargetType, GeneratedType candidateAugType) {
        return candidateAugType.getImplements().stream()
                .filter(t -> t instanceof ParameterizedType)
                .filter(t -> t.getPackageName().equals(Augmentation.class.getPackage().getName()))
                .filter(t -> t.getName().equals(Augmentation.class.getSimpleName()))
                .filter(t -> ((ParameterizedType) t).getActualTypeArguments().length == 1)
                .filter(t -> ((ParameterizedType) t).getActualTypeArguments()[0] instanceof ReferencedTypeImpl)
                .anyMatch(t -> ((ParameterizedType) t).getActualTypeArguments()[0]
                        .getFullyQualifiedName().equals(augTargetType.getFullyQualifiedName()));
    }

    static GroupingDefinition findGroupingDefinition(UsesNode use, SchemaContext context) {
        return context.getGroupings()
                .stream()
                .filter(a -> a.getQName().equals(use.getGroupingPath().getLastComponent()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unable to find grouping definition for " + use));
    }

    /**
     * Construct type path for an augmentation.
     */
    static AbstractMap.SimpleEntry<SchemaPath, LinkedHashMap<String, String>> constructParentTypePaths(
            AugmentationMeta currentAugMeta,
            SchemaContext context,
            List<Type> types) {

        SchemaPath currentPath = SchemaPath.ROOT;

        LinkedHashMap<String, String> parentClasses = new LinkedHashMap<>();
        for (QName qname : currentAugMeta.targetPathWithGroupings.getPathFromRoot()) {
            currentPath = currentPath.createChild(qname);
            SchemaNode node = findNodeInSchemaCtx(currentPath, context);

            // Add augment classes to the path
            if (Iterables.size(currentPath.getPathFromRoot()) > 1) {
                QName parentQName = currentPath.getParent().getLastComponent();

                // Resolve groupings in the schema path
                SchemaPath parentSchemaPath = getPathWithResolvedGroupings(currentAugMeta, parentQName);

                if (!parentSchemaPath.equals(SchemaPath.ROOT)) {

                    SchemaNode parent = findNodeInSchemaCtx(parentSchemaPath, context);

                    if (parent != null) {
                        DataNodeContainer realParent = switchToAugmentNode(node, (DataNodeContainer) parent);
                        if (realParent instanceof AugmentationSchemaNode) {
                            AugmentationSchemaNode intermediateAugNode = (AugmentationSchemaNode) realParent;
                            AugmentationMeta intermediateAugMeta = extractAugMeta(intermediateAugNode, context, types);

                            DataSchemaNode anyChildOfIntermediateAug = intermediateAugNode.getChildNodes().iterator()
                                    .next();
                            Module intermediateModule = findModuleByQname(anyChildOfIntermediateAug.getQName(),
                                    context);
                            Type augPathType = findAugType(types, intermediateAugNode,
                                    intermediateAugMeta.targetType, intermediateModule);
                            parentClasses.put(augPathType.getFullyQualifiedName(),
                                    IdsClassTemplate.IID_AUGMENTATION_METHOD);
                        }
                    }
                }
            }

            // Skip grouping nodes, they are not part of IID
            if (node instanceof GroupingDefinition) {
                continue;
            }

            String fqn = getFqn((DataSchemaNode) node, currentPath);
            Optional<Type> targetType = findType(Optional.empty(), (DataSchemaNode) node, currentPath, fqn, types);
            parentClasses.put(targetType.get().getFullyQualifiedName(), IdsClassTemplate.IID_CHILD_METHOD);
        }

        return new AbstractMap.SimpleEntry<>(currentPath, parentClasses);
    }

    private static SchemaPath getPathWithResolvedGroupings(AugmentationMeta currentAugMeta, QName parentQName) {
        int parentIndex = Iterables.indexOf(currentAugMeta.originalTargetPath.getPathFromRoot(),
            q -> q.equals(parentQName));
        Iterable<QName> parentPath = Iterables.limit(currentAugMeta.originalTargetPath.getPathFromRoot(),
                parentIndex + 1);
        return SchemaPath.create(parentPath, true);
    }

    static AugmentationMeta extractAugMeta(AugmentationSchemaNode augNode, SchemaContext context, List<Type> types) {
        SchemaPath targetPath = augNode.getTargetPath();
        DataSchemaNode targetNode = (DataSchemaNode) SchemaContextUtil.findNodeInSchemaContext(context,
                targetPath.getPathFromRoot());

        List<QName> fixedPath = getAugmentationTargetTypePath(targetPath, context);
        SchemaPath fixedTargetPath = SchemaPath.create(fixedPath, true);
        String fqn = getFqn(targetNode, fixedTargetPath);
        Type targetType = findType(Optional.empty(), targetNode, fixedTargetPath, fqn, types).get();

        return new AugmentationMeta(fixedTargetPath, targetPath, fqn, targetType);
    }

    private static List<QName> getAugmentationTargetTypePath(SchemaPath targetPath, SchemaContext context) {
        List<QName> typePathArgs;
        List<QName> fixedPath = Lists.newArrayList();

        SchemaPath childPath = SchemaPath.ROOT;

        for (QName qname : targetPath.getPathFromRoot()) {
            SchemaPath parentPath = childPath;
            childPath = childPath.createChild(qname);
            SchemaNode childNode = SchemaContextUtil.findNodeInSchemaContext(context, childPath.getPathFromRoot());

            DataNodeContainer parentNode = parentPath.equals(SchemaPath.ROOT)
                    ? findModuleByQname(childNode.getQName(), context)
                    : (DataNodeContainer) SchemaContextUtil.findNodeInSchemaContext(context,
                            parentPath.getPathFromRoot());

            parentNode = switchToAugmentNode(childNode, parentNode);

            typePathArgs = Lists.newArrayList(childPath.getPathFromRoot());
            fixTypePathForGroupings(parentNode, (DataSchemaNode) childNode, typePathArgs, context);

            fixedPath.add(Iterables.getLast(typePathArgs));
            if (!typePathArgs.equals(Lists.newArrayList(childPath.getPathFromRoot()))) {
                fixedPath.add(childNode.getQName());
            }
        }

        return fixedPath;
    }

    /**
     * If childNode is from an augment, extract that AugmentationSchema from parent.
     */
    private static DataNodeContainer switchToAugmentNode(SchemaNode childNode, DataNodeContainer parentNode) {
        if (parentNode instanceof AugmentationTarget) {
            Optional<AugmentationSchemaNode> augParentByUses = ((AugmentationTarget) parentNode)
                    .getAvailableAugmentations().stream()
                    .filter(a -> a.getUses().stream()
                            .anyMatch(u -> u.getGroupingPath().getLastComponent().equals(childNode.getQName())))
                    .findFirst();

            if (augParentByUses.isPresent()) {
                return augParentByUses.get();
            }

            Optional<AugmentationSchemaNode> augParent = ((AugmentationTarget) parentNode)
                    .getAvailableAugmentations().stream()
                    .filter(a -> a.getChildNodes().stream().anyMatch(c -> c.getQName().equals(childNode.getQName())))
                    .findFirst();

            if (augParent.isPresent()) {
                return augParent.get();
            }
        }

        return parentNode;
    }

    /**
     * Check if type is from grouping and if so, fix the type path by replacing the usage path with grouping
     * definition path.
     * <p>
     * Since types for groupings are generated at their definition place rather than place of use.
     * </p>
     */
    static void fixTypePathForGroupings(DataNodeContainer parent,
                                        DataSchemaNode dataSchemaNode,
                                        List<QName> typePathArgs,
                                        SchemaContext context) {
        if (dataSchemaNode.isAddedByUses()) {
            Set<UsesNode> uses = parent.getUses();

            if (uses.isEmpty()) {
                // Not direct child of grouping, no type fix necessary
            } else {
                for (UsesNode use : uses) {
                    GroupingDefinition groupingDefinition = findGroupingDefinition(use, context);
                    Optional<DataSchemaNode> foundChild = groupingDefinition.getChildNodes()
                            .stream()
                            .filter(a -> a.getQName()
                                    .getLocalName()
                                    .equals(dataSchemaNode.getQName()
                                            .getLocalName()))
                            .findFirst();
                    if (foundChild.isPresent()) {
                        typePathArgs.clear();
                        typePathArgs.add(use.getGroupingPath()
                                .getLastComponent());
                        // Since there can be uses in grouping, check the path recursively
                        fixTypePathForGroupings(groupingDefinition, foundChild.get(), typePathArgs, context);
                        break;
                    }
                }
            }
        }
    }

    static class AugmentationMeta {
        final SchemaPath targetPathWithGroupings;
        final SchemaPath originalTargetPath;
        final Type targetType;

        AugmentationMeta(SchemaPath targetPathWithGroupings,
                         SchemaPath originalTargetPath,
                         String fqn,
                         Type targetType) {
            this.targetPathWithGroupings = targetPathWithGroupings;
            this.originalTargetPath = originalTargetPath;
            this.targetType = targetType;
        }
    }
}
