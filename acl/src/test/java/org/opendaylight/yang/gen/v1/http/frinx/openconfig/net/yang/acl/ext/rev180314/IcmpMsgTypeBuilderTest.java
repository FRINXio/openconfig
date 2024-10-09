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

package org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.acl.ext.rev180314;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.acl.ext.rev180314.IcmpMsgType.Enumeration;

class IcmpMsgTypeBuilderTest {

    @Test
    void parse_any() {
        IcmpMsgType parsedValue = IcmpMsgTypeBuilder.getDefaultInstance("any");
        assertTrue(parsedValue.getEnumeration() != null &&
            parsedValue.getEnumeration().equals(Enumeration.ANY)
        );
    }

    @Test
    void parse_ANY() {
        IcmpMsgType parsedValue = IcmpMsgTypeBuilder.getDefaultInstance("ANY");
        assertTrue(parsedValue.getEnumeration() != null &&
            parsedValue.getEnumeration().equals(Enumeration.ANY)
        );
    }

    @Test
    void parse_wrong() {
        assertThrows(IllegalArgumentException.class, () -> {
            IcmpMsgType parsedValue = IcmpMsgTypeBuilder.getDefaultInstance("blaaahi");
        });
    }

    @Test
    void parse_short() {
        IcmpMsgType parsedValue = IcmpMsgTypeBuilder.getDefaultInstance("5");
        assertTrue(parsedValue.getUint8() != null &&
            parsedValue.getUint8().equals(((short) 5))
        );

        parsedValue = IcmpMsgTypeBuilder.getDefaultInstance("95");
        assertTrue(parsedValue.getUint8() != null &&
            parsedValue.getUint8().equals(((short) 95))
        );

        parsedValue = IcmpMsgTypeBuilder.getDefaultInstance("125");
        assertTrue(parsedValue.getUint8() != null &&
            parsedValue.getUint8().equals(((short) 125))
        );

        parsedValue = IcmpMsgTypeBuilder.getDefaultInstance("255");
        assertTrue(parsedValue.getUint8() != null &&
            parsedValue.getUint8().equals(((short) 255))
        );
    }
}
