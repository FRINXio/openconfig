/*
 * Copyright © 2020 Frinx and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
