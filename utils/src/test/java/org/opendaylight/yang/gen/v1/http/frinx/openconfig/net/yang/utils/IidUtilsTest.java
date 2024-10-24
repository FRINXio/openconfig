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

package org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.interfaces.juniper.extention.rev181204.Config1;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.interfaces.rev161222.interfaces.top.Interfaces;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.interfaces.rev161222.interfaces.top.interfaces.Interface;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.interfaces.rev161222.interfaces.top.interfaces.InterfaceKey;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.interfaces.rev161222.subinterfaces.top.Subinterfaces;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.interfaces.rev161222.subinterfaces.top.subinterfaces.Subinterface;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.interfaces.rev161222.subinterfaces.top.subinterfaces.SubinterfaceKey;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.interfaces.rev161222.subinterfaces.top.subinterfaces.subinterface.Config;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

class IidUtilsTest {

    private InstanceIdentifier<Config1> WO_KEYS_ID = InstanceIdentifier.create(Interfaces.class)
            .child(Interface.class)
            .child(Subinterfaces.class)
            .child(Subinterface.class)
            .child(Config.class)
            .augmentation(Config1.class);

    private InstanceIdentifier EXPECTED_IID = InstanceIdentifier.create(Interfaces.class)
            .child(Interface.class, new InterfaceKey("GigabitEthernet0/0/0/0"))
            .child(Subinterfaces.class)
            .child(Subinterface.class, new SubinterfaceKey(0L))
            .child(Config.class)
            .augmentation(Config1.class);

    @Test
    void testCreateInstanceIdentifierWithKeys() {
        InstanceIdentifier actualIID = IidUtils
                .createIid(WO_KEYS_ID,
                        new InterfaceKey("GigabitEthernet0/0/0/0"),
                        new SubinterfaceKey(0L));

        assertEquals(EXPECTED_IID, actualIID);
        assertEquals(EXPECTED_IID.getTargetType(), actualIID.getTargetType());
    }

    @Test
    void testCreateInstanceMissingKey() {
        assertThrows(NullPointerException.class, () -> {
            IidUtils.createIid(WO_KEYS_ID, new InterfaceKey("GigabitEthernet0/0/0/0"));
        });
    }

    @Test
    void testCreateInstanceNonMatchingKey() {
        assertThrows(NullPointerException.class, () -> {
            InstanceIdentifier<Interface> woKeysIID = InstanceIdentifier.create(Interfaces.class)
                    .child(Interface.class);
            IidUtils.createIid(woKeysIID, new SubinterfaceKey(0L));
        });
    }

}
