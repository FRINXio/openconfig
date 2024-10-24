package org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.bgp.policy.rev170730;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.bgp.policy.rev170730.CommunitySetConfig.CommunityMember;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.bgp.types.rev170202.BgpStdCommunityType;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.bgp.types.rev170202.BgpStdCommunityTypeString;

/**
 * The purpose of generated class in src/main/java for Union types is to create new instances of unions from a string representation.
 * In some cases it is very difficult to automate it since there can be unions such as (uint32 - uint16), or (string - uint32).
 *
 * The reason behind putting it under src/main/java is:
 * This class is generated in form of a stub and needs to be finished by the user. This class is generated only once to prevent
 * loss of user code.
 *
 */
public class CommunitySetConfigCommunityMemberBuilder {

    public static CommunityMember getDefaultInstance(java.lang.String defaultValue) {
        // FIXME distinguish between a number and number:number format for community
        // currently using string representation even for numbers
        return new CommunityMember(new BgpStdCommunityType(new BgpStdCommunityTypeString(defaultValue)));
    }

}
