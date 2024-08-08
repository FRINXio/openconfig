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

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.apache.commons.lang3.StringUtils;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.binding.YangModuleInfo;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.model.api.AugmentationSchemaNode;
import org.opendaylight.yangtools.yang.model.api.DataSchemaNode;
import org.opendaylight.yangtools.yang.model.api.Module;
import org.opendaylight.yangtools.yang.model.api.SchemaPath;


final class IdsClassTemplate {
    static final String IID_AUGMENTATION_METHOD = "augmentation";
    static final String IID_CHILD_METHOD = "child";
    static final String CLS_NAME = "IIDs";
    private static final String PACKAGE_TEMPLATE = "package %s;%n%n";
    private static final String IMPORT_TEMPLATE = "import %s;%n%n";
    private static final String CLASS_DECLARATION_TEMPLATE = "public final class " + CLS_NAME + " {\n";
    private static final String CLOSING_BRACE = "}";
    private static final String MODULE_START = "%n\t// Module: %s %s%n%n";
    private static final String MODULE_AUG_START = "%n\t// Module(Augmentations): %s %s%n%n";
    private static final String CONSTANT_JAVADOC = "\t/**%n\t * %s%n\t**/%n";
    private static final String CONSTANT_DEFINITION = "\tpublic static final InstanceIdentifier<%s> %s = %s(%s.class);";
    private static final String CONSTANT_DEFINITION_YANG_MODULE_INFO = "\tpublic static final YangModuleInfo %s = "
            + "%s.$YangModuleInfoImpl.getInstance()" + ";%n";

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
        return module.getName().toUpperCase(Locale.ROOT).replaceAll("-", "_");
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
            String method = dataSchemaNode instanceof AugmentationSchemaNode
                    ? IID_AUGMENTATION_METHOD
                    : IID_CHILD_METHOD;
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
