package org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.bgp.policy.rev170730;

import java.util.Locale;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.types.inet.rev170403.IpAddress;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.types.inet.rev170403.IpAddressBuilder;

/**
 * The purpose of generated class in src/main/java for Union types is to create new instances of unions from a string representation.
 * In some cases it is very difficult to automate it since there can be unions such as (uint32 - uint16), or (string - uint32).
 *
 * The reason behind putting it under src/main/java is:
 * This class is generated in form of a stub and needs to be finished by the user. This class is generated only once to prevent
 * loss of user code.
 *
 */
public class BgpNextHopTypeBuilder {

    public static BgpNextHopType getDefaultInstance(java.lang.String defaultValue) {
        try {
            final BgpNextHopType.Enumeration nextHopEnum = BgpNextHopType.Enumeration.valueOf(defaultValue.toUpperCase(Locale.ROOT));
            return new BgpNextHopType(nextHopEnum);
        } catch (final IllegalArgumentException e) {
            try {
                final IpAddress ipAddress = IpAddressBuilder.getDefaultInstance(defaultValue);
                return new BgpNextHopType(ipAddress);
            } catch (final IllegalArgumentException e1) {
                throw new IllegalArgumentException("Cannot create BgpNextHopType from " + defaultValue);
            }
        }
    }

}
