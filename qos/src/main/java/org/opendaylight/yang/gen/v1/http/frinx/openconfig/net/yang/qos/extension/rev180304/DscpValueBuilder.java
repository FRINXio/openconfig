package org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.qos.extension.rev180304;

/**
 * The purpose of generated class in src/main/java for Union types is to create new instances of unions from a string representation.
 * In some cases it is very difficult to automate it since there can be unions such as (uint32 - uint16), or (string - uint32).
 *
 * The reason behind putting it under src/main/java is:
 * This class is generated in form of a stub and needs to be finished by the user. This class is generated only once to prevent
 * loss of user code.
 *
 */
public class DscpValueBuilder {

    private DscpValueBuilder() {
        //Exists only to defeat instantiation.
    }

    public static org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.qos.extension.rev180304.DscpValue getDefaultInstance(java.lang.String defaultValue) {
        try {
            return new DscpValue(new DscpNumber(Short.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return new DscpValue(getDscpEnum(defaultValue));
        }
    }

    private static DscpEnumeration getDscpEnum(String name) {
        for (DscpEnumeration enumeration: DscpEnumeration.values()) {
            if (name.equals(enumeration.getName())) {
                return enumeration;
            }
        }
        return null;
    }

}
