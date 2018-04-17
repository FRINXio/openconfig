/*
 * Copyright © 2018 FRINX s.r.o. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the FRINX ODL End User License Agreement which accompanies this distribution,
 * and is available at https://frinx.io/wp-content/uploads/2017/01/EULA_ODL_20170104_v102.pdf
 */

package org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.acl.ext.rev180314;

import org.junit.Assert;
import org.junit.Test;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.acl.ext.rev180314.IcmpMsgType.Enumeration;

public class IcmpMsgTypeBuilderTest {

    @Test
    public void parse_any() {
        IcmpMsgType parsedValue = IcmpMsgTypeBuilder.getDefaultInstance("any");
        Assert.assertTrue(parsedValue.getEnumeration() != null &&
            parsedValue.getEnumeration().equals(Enumeration.ANY)
        );
    }

    @Test
    public void parse_ANY() {
        IcmpMsgType parsedValue = IcmpMsgTypeBuilder.getDefaultInstance("ANY");
        Assert.assertTrue(parsedValue.getEnumeration() != null &&
            parsedValue.getEnumeration().equals(Enumeration.ANY)
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void parse_wrong() {
        IcmpMsgType parsedValue = IcmpMsgTypeBuilder.getDefaultInstance("blaaahi");
    }

    @Test
    public void parse_short() {
        IcmpMsgType parsedValue = IcmpMsgTypeBuilder.getDefaultInstance("5");
        Assert.assertTrue(parsedValue.getUint8() != null &&
            parsedValue.getUint8().equals(((short) 5))
        );

        parsedValue = IcmpMsgTypeBuilder.getDefaultInstance("95");
        Assert.assertTrue(parsedValue.getUint8() != null &&
            parsedValue.getUint8().equals(((short) 95))
        );

        parsedValue = IcmpMsgTypeBuilder.getDefaultInstance("125");
        Assert.assertTrue(parsedValue.getUint8() != null &&
            parsedValue.getUint8().equals(((short) 125))
        );

        parsedValue = IcmpMsgTypeBuilder.getDefaultInstance("255");
        Assert.assertTrue(parsedValue.getUint8() != null &&
            parsedValue.getUint8().equals(((short) 255))
        );
    }
}
