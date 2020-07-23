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
