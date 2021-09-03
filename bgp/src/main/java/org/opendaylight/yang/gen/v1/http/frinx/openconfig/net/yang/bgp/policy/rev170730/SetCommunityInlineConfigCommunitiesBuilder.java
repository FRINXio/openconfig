package org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.bgp.policy.rev170730;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.bgp.policy.rev170730.SetCommunityInlineConfig.Communities;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.bgp.types.rev170202.BgpStdCommunityType;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.bgp.types.rev170202.BgpStdCommunityTypeString;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.bgp.types.rev170202.BgpStdCommunityTypeUnit32;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.bgp.types.rev170202.NOADVERTISE;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.bgp.types.rev170202.NOEXPORT;

/**
 * The purpose of generated class in src/main/java for Union types is to create new instances of unions from a string representation.
 * In some cases it is very difficult to automate it since there can be unions such as (uint32 - uint16), or (string - uint32).
 *
 * The reason behind putting it under src/main/java is:
 * This class is generated in form of a stub and needs to be finished by the user. This class is generated only once to prevent
 * loss of user code.
 *
 */
public class SetCommunityInlineConfigCommunitiesBuilder {

    public static Communities getDefaultInstance(java.lang.String defaultValue) {
        if (defaultValue.matches("\\d+:\\d+")) {
            return new SetCommunityInlineConfig.Communities(new BgpStdCommunityType(
                    new BgpStdCommunityTypeString(defaultValue)));
        }
        else if (defaultValue.matches("\\d+")) {
            return new SetCommunityInlineConfig.Communities(new BgpStdCommunityType(
                    new BgpStdCommunityTypeUnit32(Long.valueOf(defaultValue))));
        }
        else if (defaultValue.equals("no-export")) {
            return new SetCommunityInlineConfig.Communities(NOEXPORT.class);
        } else if (defaultValue.equals("no-advertise")) {
            return new SetCommunityInlineConfig.Communities(NOADVERTISE.class);
        }
        throw new IllegalArgumentException("Did not match community for value: " + defaultValue);
    }

}
