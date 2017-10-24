/*
 * Copyright © 2017 Frinx and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package io.frinx.openconfig.network.instance;

import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.network.instance.rev170228.network.instance.top.network.instances.NetworkInstanceKey;

public class NetworInstance {

    public static final String DEFAULT_NETWORK_NAME = "default";
    public static final NetworkInstanceKey DEFAULT_NETWORK = new NetworkInstanceKey(DEFAULT_NETWORK_NAME);

}
