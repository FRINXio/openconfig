package org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.mpls.extension.rev171024;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.mpls.extension.rev171024.MplsRsvpSubscriptionConfig.Bandwidth;

/**
 * The purpose of generated class in src/main/java for Union types is to create new instances of unions from a string representation.
 * In some cases it is very difficult to automate it since there can be unions such as (uint32 - uint16), or (string - uint32).
 *
 * The reason behind putting it under src/main/java is:
 * This class is generated in form of a stub and needs to be finished by the user. This class is generated only once to prevent
 * loss of user code.
 *
 */
public class MplsRsvpSubscriptionConfigBandwidthBuilder {

    private static final String DEFAULT = "default";

    public static Bandwidth getDefaultInstance(String defaultValue) {
        if (DEFAULT.equals(defaultValue)) {
            return new Bandwidth(DEFAULT);
        }
        try {
            return new Bandwidth(Long.valueOf(defaultValue));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Bandwidth value is not a number, nor '%s', supplied value is '%s'", DEFAULT, defaultValue));
        }
    }
}
