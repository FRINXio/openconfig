package org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.packet.match.types.rev170526;

import java.util.regex.Pattern;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.packet.match.types.rev170526.PortNumRange.Enumeration;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.types.inet.rev170403.PortNumber;

/**
 * The purpose of generated class in src/main/java for Union types is to create new instances of unions from a string representation.
 * In some cases it is very difficult to automate it since there can be unions such as (uint32 - uint16), or (string - uint32).
 *
 * The reason behind putting it under src/main/java is:
 * This class is generated in form of a stub and needs to be finished by the user. This class is generated only once to prevent
 * loss of user code.
 */
public class PortNumRangeBuilder {

    private static final Pattern PATTERN_RANGE = Pattern.compile(
        "(6553[0-5]|655[0-2][0-9]|65[0-4][0-9][0-9]|6[0-4][0-9][0-9][0-9]|[0-5]?[0-9]?[0-9]?[0-9]?[0-9]?)"
            + "\\.\\."
            + "(6553[0-5]|655[0-2][0-9]|65[0-4][0-9][0-9]|6[0-4][0-9][0-9][0-9]|[0-5]?[0-9]?[0-9]?[0-9]?[0-9]?)");
    private static final Pattern PATTERN_PORT = Pattern.compile(
        "(6553[0-5]|655[0-2][0-9]|65[0-4][0-9][0-9]|6[0-4][0-9][0-9][0-9]|[0-5]?[0-9]?[0-9]?[0-9]?[0-9]?)");
    private static final String ANY = "any";

    public static PortNumRange getDefaultInstance(java.lang.String defaultValue) {
        if (PATTERN_RANGE.matcher(defaultValue).matches()) {
            return new PortNumRange(defaultValue);

        } else if (PATTERN_PORT.matcher(defaultValue).matches()) {
            return new PortNumRange(new PortNumber(Integer.parseInt(defaultValue)));

        } else if (ANY.equalsIgnoreCase(defaultValue)) {
            return new PortNumRange(Enumeration.ANY);
        }

        throw new IllegalArgumentException(
            "Invalid port range entered: %s, should contains 'any', 'ANY', "
                + "or two numbers in range <0,65535> separated by '..' ."
        );
    }

}
