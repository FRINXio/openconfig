package io.frinx.binding.ids;

import java.util.Optional;
import java.util.stream.StreamSupport;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.model.api.AugmentationSchema;
import org.opendaylight.yangtools.yang.model.api.DataSchemaNode;
import org.opendaylight.yangtools.yang.model.api.Module;
import org.opendaylight.yangtools.yang.model.api.SchemaPath;

final class IdsClassTemplate {

    static final String CLS_NAME = "IIDs";
    private static final String PACKAGE_TEMPLATE = "package %s;\n\n";
    private static final String IMPORT_TEMPLATE = "import %s;\n\n";
    private static final String CLASS_DECLARATION_TEMPLATE = "public final class " + CLS_NAME + " {\n";
    private static final String CLOSING_BRACE = "}";
    private static final String MODULE_START = "\n\t// Module: %s %s\n\n";
    private static final String CONSTANT_JAVADOC = "\t/**\n\t * %s\n\t**/\n";
    private static final String CONSTANT_DEFINITION = "\tpublic static final InstanceIdentifier<%s> %s = %s(%s.class);\n";

    private final StringBuilder result = new StringBuilder();

    IdsClassTemplate(String packageName) {
        result.append(String.format(PACKAGE_TEMPLATE, packageName));
        result.append(String.format(IMPORT_TEMPLATE, InstanceIdentifier.class.getName()));
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

    void addModuleStart(Module module) {
        result.append(String.format(MODULE_START, module.getQNameModule().getNamespace().toString(), module.getName()));
    }

    void addId(Optional<String> parentVarName, DataSchemaNode dataSchemaNode,
               SchemaPath schemaPath, String varName, String fqn) {

        String parentCode = "InstanceIdentifier.create";
        if (parentVarName.isPresent()) {
            String method = dataSchemaNode instanceof AugmentationSchema ? "augmentation" : "child";
            parentCode = parentVarName.get() + "." + method;
        }

        result.append(String.format(CONSTANT_JAVADOC, toConstantHeader(schemaPath)));
        result.append(String.format(CONSTANT_DEFINITION, fqn, varName, parentCode, fqn));
    }

    /**
     * Create human readable representation of schema path
     */
    private static String toConstantHeader(SchemaPath schemaPath) {
        return StreamSupport.stream(schemaPath.getPathFromRoot().spliterator(), false)
                .map(QName::getLocalName)
                .reduce("", (s, s2) -> s + "/" + s2);
    }
}
