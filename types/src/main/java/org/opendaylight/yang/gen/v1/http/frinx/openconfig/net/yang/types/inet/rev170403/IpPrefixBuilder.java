package org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.types.inet.rev170403;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The purpose of generated class in src/main/java for Union types is to create new instances of unions from a string representation.
 * In some cases it is very difficult to automate it since there can be unions such as (uint32 - uint16), or (string - uint32).
 *
 * The reason behind putting it under src/main/java is:
 * This class is generated in form of a stub and needs to be finished by the user. This class is generated only once to prevent
 * loss of user code.
 *
 */
public class IpPrefixBuilder {

    private static final Pattern IPV4_PATTERN = Pattern.compile("(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])/(([0-9])|([1-2][0-9])|(3[0-2]))");
    private static final Pattern IPV6_PATTERN1 = Pattern.compile("((:|[0-9a-fA-F]{0,4}):)([0-9a-fA-F]{0,4}:){0,5}((([0-9a-fA-F]{0,4}:)?(:|[0-9a-fA-F]{0,4}))|(((25[0-5]|2[0-4][0-9]|[01]?[0-9]?[0-9])\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9]?[0-9])))(/(([0-9])|([0-9]{2})|(1[0-1][0-9])|(12[0-8])))");
    private static final Pattern IPV6_PATTERN2 = Pattern.compile("(([^:]+:){6}(([^:]+:[^:]+)|(.*\\..*)))|((([^:]+:)*[^:]+)?::(([^:]+:)*[^:]+)?)(/.+)");

    private IpPrefixBuilder() {

    }

    public static IpPrefix getDefaultInstance(final String defaultValue) {
        final Matcher ipv4Matcher = IPV4_PATTERN.matcher(defaultValue);

        if (ipv4Matcher.matches()) {
            if (IPV6_PATTERN1.matcher(defaultValue).matches() && IPV6_PATTERN2.matcher(defaultValue).matches()) {
                throw new IllegalArgumentException(
                    String.format("Cannot create IpPrefix from \"%s\", matches both %s and %s",
                        defaultValue, Ipv4Address.class.getSimpleName(), Ipv6Address.class.getSimpleName()));

            }
            return new IpPrefix(new Ipv4Prefix(defaultValue));
        } else if (IPV6_PATTERN1.matcher(defaultValue).matches() && IPV6_PATTERN2.matcher(defaultValue).matches()) {
            return new IpPrefix(new Ipv6Prefix(defaultValue));
        } else {
            throw new IllegalArgumentException("Cannot create IpPrefix from " + defaultValue);
        }
    }


}
