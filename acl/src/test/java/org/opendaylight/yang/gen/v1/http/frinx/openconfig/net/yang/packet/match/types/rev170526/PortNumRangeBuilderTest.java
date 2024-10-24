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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.packet.match.types.rev170526.PortNumRange.Enumeration;

class PortNumRangeBuilderTest {

    @Test
    void parse_any() {
        final PortNumRange parsedValue = PortNumRangeBuilder.getDefaultInstance("any");
        assertTrue(parsedValue.getEnumeration() != null &&
            parsedValue.getEnumeration().equals(Enumeration.ANY)
        );
    }

    @Test
    void parse_ANY() {
        final PortNumRange parsedValue = PortNumRangeBuilder.getDefaultInstance("ANY");
        assertTrue(parsedValue.getEnumeration() != null &&
            parsedValue.getEnumeration().equals(Enumeration.ANY)
        );
    }

    @Test
    void parse_wrong() {
        assertThrows(IllegalArgumentException.class, () -> {
            final PortNumRange parsedValue = PortNumRangeBuilder.getDefaultInstance("blaaahi");
        });
    }

    @Test
    void parse_port() {
        PortNumRange parsedValue = PortNumRangeBuilder.getDefaultInstance("5");
        assertTrue(parsedValue.getPortNumber().getValue() != null &&
            parsedValue.getPortNumber().getValue().equals(5)
        );

        parsedValue = PortNumRangeBuilder.getDefaultInstance("95");
        assertTrue(parsedValue.getPortNumber().getValue() != null &&
            parsedValue.getPortNumber().getValue().equals(95)
        );

        parsedValue = PortNumRangeBuilder.getDefaultInstance("125");
        assertTrue(parsedValue.getPortNumber().getValue() != null &&
            parsedValue.getPortNumber().getValue().equals(125)
        );

        parsedValue = PortNumRangeBuilder.getDefaultInstance("255");
        assertTrue(parsedValue.getPortNumber().getValue() != null &&
            parsedValue.getPortNumber().getValue().equals(255)
        );
    }

    @Test
    void parse_port_range() {
        PortNumRange parsedValue = PortNumRangeBuilder.getDefaultInstance("5..5");
        assertTrue(parsedValue.getString() != null &&
            parsedValue.getString().equals("5..5")
        );

        parsedValue = PortNumRangeBuilder.getDefaultInstance("95..125");
        assertTrue(parsedValue.getString() != null &&
            parsedValue.getString().equals("95..125")
        );

        parsedValue = PortNumRangeBuilder.getDefaultInstance("125..65535");
        assertTrue(parsedValue.getString() != null &&
            parsedValue.getString().equals("125..65535")
        );

        parsedValue = PortNumRangeBuilder.getDefaultInstance("125..95");
        assertTrue(parsedValue.getString() != null &&
            parsedValue.getString().equals("125..95")
        );
    }
}
