/*
 * Copyright © 2018 FRINX s.r.o. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the FRINX ODL End User License Agreement which accompanies this distribution,
 * and is available at https://frinx.io/wp-content/uploads/2017/01/EULA_ODL_20170104_v102.pdf
 */

package org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.packet.match.types.rev170526;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.packet.match.types.rev170526.PortNumRange.Enumeration;

public class PortNumRangeBuilderTest {

    @Test
    public void parse_any() {
        final PortNumRange parsedValue = PortNumRangeBuilder.getDefaultInstance("any");
        Assert.assertTrue(parsedValue.getEnumeration() != null &&
            parsedValue.getEnumeration().equals(Enumeration.ANY)
        );
    }

    @Test
    public void parse_ANY() {
        final PortNumRange parsedValue = PortNumRangeBuilder.getDefaultInstance("ANY");
        Assert.assertTrue(parsedValue.getEnumeration() != null &&
            parsedValue.getEnumeration().equals(Enumeration.ANY)
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void parse_wrong() {
        final PortNumRange parsedValue = PortNumRangeBuilder.getDefaultInstance("blaaahi");
    }

    @Test
    public void parse_port() {
        PortNumRange parsedValue = PortNumRangeBuilder.getDefaultInstance("5");
        Assert.assertTrue(parsedValue.getPortNumber().getValue() != null &&
            parsedValue.getPortNumber().getValue().equals(5)
        );

        parsedValue = PortNumRangeBuilder.getDefaultInstance("95");
        Assert.assertTrue(parsedValue.getPortNumber().getValue() != null &&
            parsedValue.getPortNumber().getValue().equals(95)
        );

        parsedValue = PortNumRangeBuilder.getDefaultInstance("125");
        Assert.assertTrue(parsedValue.getPortNumber().getValue() != null &&
            parsedValue.getPortNumber().getValue().equals(125)
        );

        parsedValue = PortNumRangeBuilder.getDefaultInstance("255");
        Assert.assertTrue(parsedValue.getPortNumber().getValue() != null &&
            parsedValue.getPortNumber().getValue().equals(255)
        );
    }

    @Test
    public void parse_port_range() {
        PortNumRange parsedValue = PortNumRangeBuilder.getDefaultInstance("5..5");
        Assert.assertTrue(parsedValue.getString() != null &&
            parsedValue.getString().equals("5..5")
        );

        parsedValue = PortNumRangeBuilder.getDefaultInstance("95..125");
        Assert.assertTrue(parsedValue.getString() != null &&
            parsedValue.getString().equals("95..125")
        );

        parsedValue = PortNumRangeBuilder.getDefaultInstance("125..65535");
        Assert.assertTrue(parsedValue.getString() != null &&
            parsedValue.getString().equals("125..65535")
        );

        parsedValue = PortNumRangeBuilder.getDefaultInstance("125..95");
        Assert.assertTrue(parsedValue.getString() != null &&
            parsedValue.getString().equals("125..95")
        );
    }
}
