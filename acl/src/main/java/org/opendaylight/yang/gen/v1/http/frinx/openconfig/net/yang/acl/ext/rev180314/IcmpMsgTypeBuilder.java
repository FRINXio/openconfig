package org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.acl.ext.rev180314;

import java.util.regex.Pattern;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.acl.ext.rev180314.IcmpMsgType.Enumeration;

/**
 * The purpose of generated class in src/main/java for Union types is to create new instances of unions from a string representation.
 * In some cases it is very difficult to automate it since there can be unions such as (uint32 - uint16), or (string - uint32).
 *
 * The reason behind putting it under src/main/java is:
 * This class is generated in form of a stub and needs to be finished by the user. This class is generated only once to prevent
 * loss of user code.
 *
 */
public class IcmpMsgTypeBuilder {

    private static final Pattern PATTERN_SHORT_NUMBER = Pattern.compile("(25[0-5]|2[0-4][0-9]|[0-1]?[0-9]?[0-9])");
    private static final String ANY = "any";

    public static org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.acl.ext.rev180314.IcmpMsgType getDefaultInstance(java.lang.String defaultValue) {

        if (PATTERN_SHORT_NUMBER.matcher(defaultValue).matches()) {
            return new IcmpMsgType(Short.parseShort(defaultValue));
        } else {

            if (ANY.equalsIgnoreCase(defaultValue)) {
                return new IcmpMsgType(Enumeration.ANY);
            }
        }

        throw new IllegalArgumentException(
            "Invalid icmp-msg-type entered: %s, should contains 'any', 'ANY', or number from 0 to 255"
        );
    }

}
