/*
 * Copyright © 2018 FRINX s.r.o. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the FRINX ODL End User License Agreement which accompanies this distribution,
 * and is available at https://frinx.io/wp-content/uploads/2017/01/EULA_ODL_20170104_v102.pdf
 */

package org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.utils;

import org.junit.Assert;
import org.junit.Test;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.interfaces.juniper.extention.rev181204.Config1;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.interfaces.rev161222.interfaces.top.Interfaces;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.interfaces.rev161222.interfaces.top.interfaces.Interface;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.interfaces.rev161222.interfaces.top.interfaces.InterfaceKey;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.interfaces.rev161222.subinterfaces.top.Subinterfaces;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.interfaces.rev161222.subinterfaces.top.subinterfaces.Subinterface;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.interfaces.rev161222.subinterfaces.top.subinterfaces.SubinterfaceKey;
import org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.interfaces.rev161222.subinterfaces.top.subinterfaces.subinterface.Config;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

public class IidUtilsTest {

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
    public void testCreateInstanceIdentifierWithKeys() {
        InstanceIdentifier actualIID = IidUtils
                .createIid(WO_KEYS_ID,
                        new InterfaceKey("GigabitEthernet0/0/0/0"),
                        new SubinterfaceKey(0L));

        Assert.assertEquals(EXPECTED_IID, actualIID);
        Assert.assertEquals(EXPECTED_IID.getTargetType(), actualIID.getTargetType());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateInstanceMissingKey() {
        IidUtils.createIid(WO_KEYS_ID, new InterfaceKey("GigabitEthernet0/0/0/0"));
    }

    @Test(expected = NullPointerException.class)
    public void testCreateInstanceNonMatchingKey() {
        InstanceIdentifier<Interface> woKeysIID = InstanceIdentifier.create(Interfaces.class)
                .child(Interface.class);
        IidUtils.createIid(woKeysIID, new SubinterfaceKey(0L));
    }

}
