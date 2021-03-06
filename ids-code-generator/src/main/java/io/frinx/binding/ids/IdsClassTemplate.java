/*
 * Copyright © 2017 Frinx and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package io.frinx.binding.ids;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.apache.commons.lang3.StringUtils;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.binding.YangModuleInfo;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.model.api.AugmentationSchema;
import org.opendaylight.yangtools.yang.model.api.DataSchemaNode;
import org.opendaylight.yangtools.yang.model.api.Module;
import org.opendaylight.yangtools.yang.model.api.SchemaPath;

final class IdsClassTemplate {
    static final String IID_AUGMENTATION_METHOD = "augmentation";
    static final String IID_CHILD_METHOD = "child";
    static final String CLS_NAME = "IIDs";
    private static final String PACKAGE_TEMPLATE = "package %s;\n\n";
    private static final String IMPORT_TEMPLATE = "import %s;\n\n";
    private static final String CLASS_DECLARATION_TEMPLATE = "public final class " + CLS_NAME + " {\n";
    private static final String CLOSING_BRACE = "}";
    private static final String MODULE_START = "\n\t// Module: %s %s\n\n";
    private static final String MODULE_AUG_START = "\n\t// Module(Augmentations): %s %s\n\n";
    private static final String CONSTANT_JAVADOC = "\t/**\n\t * %s\n\t**/\n";
    private static final String CONSTANT_DEFINITION = "\tpublic static final InstanceIdentifier<%s> %s = %s(%s.class);";
    private static final String CONSTANT_DEFINITION_YANG_MODULE_INFO = "\tpublic static final YangModuleInfo %s = "
            + "%s.$YangModuleInfoImpl.getInstance()" + ";\n";

    private final StringBuilder result = new StringBuilder();

    IdsClassTemplate(String packageName) {
        result.append(String.format(PACKAGE_TEMPLATE, packageName));
        result.append(String.format(IMPORT_TEMPLATE, InstanceIdentifier.class.getName()));
        result.append(String.format(IMPORT_TEMPLATE, YangModuleInfo.class.getName()));
        result.append(CLASS_DECLARATION_TEMPLATE);
    }

    private void newLine() {
        result.append(System.lineSeparator());
    }

    String getContent() {
        newLine();
        result.append(CLOSING_BRACE);
        return result.toString();
    }

    void addModuleStart(Module module, String rootPackageName) {
        result.append(String.format(MODULE_START,
                module.getQNameModule().getNamespace().toString(),
                module.getName()));
        result.append(String.format(CONSTANT_DEFINITION_YANG_MODULE_INFO, createConstantName(module), rootPackageName));
    }

    private static String createConstantName(Module module) {
        return module.getName().toUpperCase().replaceAll("-", "_");
    }

    void addModuleAugStart(Module module) {
        result.append(String.format(MODULE_AUG_START,
                module.getQNameModule().getNamespace().toString(),
                module.getName()));
    }

    void addId(Optional<String> parentVarName,
               DataSchemaNode dataSchemaNode,
               SchemaPath schemaPath,
               String varName,
               String fqn) {

        String parentCode = "InstanceIdentifier.create";
        if (parentVarName.isPresent()) {
            String method = dataSchemaNode instanceof AugmentationSchema ? IID_AUGMENTATION_METHOD : IID_CHILD_METHOD;
            parentCode = parentVarName.get() + "." + method;
        }
        result.append(String.format(CONSTANT_JAVADOC, toConstantHeader(schemaPath)));
        result.append(String.format(CONSTANT_DEFINITION, fqn, varName, parentCode, fqn));
    }

    void addAugId(String varName,
                  String fqn,
                  LinkedHashMap<String, String> parentFqns,
                  SchemaPath currentPath) {

        Preconditions.checkArgument(parentFqns.size() >= 1);

        String parentCode = String.format("InstanceIdentifier.create(%s.class)",
                parentFqns.entrySet().iterator().next().getKey());
        for (Map.Entry<String, String> parent : Iterables.skip(parentFqns.entrySet(), 1)) {
            parentCode += String.format(".%s(%s.class)", parent.getValue(), parent.getKey());
        }

        parentCode += "." + IID_AUGMENTATION_METHOD;

        String javadoc = String.format("Augmentation[%s] for %s",
                StringUtils.substringAfterLast(fqn, "."), toConstantHeader(currentPath));
        result.append(String.format(CONSTANT_JAVADOC, javadoc));
        result.append(String.format(CONSTANT_DEFINITION, fqn, varName, parentCode, fqn));
    }

    /**
     * Create human readable representation of schema path.
     */
    private static String toConstantHeader(SchemaPath schemaPath) {
        return StreamSupport.stream(schemaPath.getPathFromRoot().spliterator(), false)
                .map(QName::getLocalName)
                .reduce("", (s1, s2) -> s1 + "/" + s2);
    }
}
