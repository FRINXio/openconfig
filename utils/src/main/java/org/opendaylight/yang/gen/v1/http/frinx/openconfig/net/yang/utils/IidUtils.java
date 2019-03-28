/*
 * Copyright © 2018 FRINX s.r.o. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the FRINX ODL End User License Agreement which accompanies this distribution,
 * and is available at https://frinx.io/wp-content/uploads/2017/01/EULA_ODL_20170104_v102.pdf
 */

package org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.utils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import org.opendaylight.yangtools.yang.binding.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class IidUtils {

    private IidUtils() {
    }

    /**
     * Creates unique identifier - path to the node that uniquely identifies all the node's parent nodes, using
     * full path to target node and mandatory keys (doesn't have to be ordered).
     *
     * @param iid  InstanceIdentifier without keys including full path to target node.
     * @param keys Identifiers (keys) assignable to specified InstanceIdentifier
     * @throws NullPointerException informing about missing key - if the necessary keys are missing or doesn't match iid
     * @return InstanceIdentifier
     */
    public static <T extends DataObject> InstanceIdentifier<T> createIid(final InstanceIdentifier<T> iid,
            final Identifier<? extends Identifiable<?>>... keys) {

        List<InstanceIdentifier.PathArgument> instanceIdentifier = Lists.newArrayList();

        Map<String, Identifier<? extends Identifiable<?>>> keysMap = Maps.newHashMap();

        for (Identifier<? extends Identifiable<?>> key : keys) {

            Class<? extends Identifier> kClass = key.getClass();
            Optional<String> typeClass = TypeToken.of(kClass).getTypes().interfaces()
                    .stream()
                    .filter(t -> t.getRawType().equals(Identifier.class))
                    .map(TypeToken::toString)
                    .map(tS -> tS.substring(tS.lastIndexOf("<") + 1, tS.lastIndexOf(">")))
                    .map(String::trim)
                    .findFirst();

            Preconditions.checkArgument(typeClass.isPresent(),
                    "Unable to determine list type for key: %s ", kClass);

            keysMap.put(typeClass.get(), key);
        }

        for (InstanceIdentifier.PathArgument pathArgument : iid.getPathArguments()) {
            Class<? extends DataObject> type = pathArgument.getType();

            if (Identifiable.class.isAssignableFrom(type)) {
                Preconditions.checkNotNull(keysMap.get(type.getName()),
                        "Key for %s is missing", type.getName());
                instanceIdentifier.add(new InstanceIdentifier.IdentifiableItem(type, keysMap.get(type.getName())));
            } else {
                instanceIdentifier.add(new InstanceIdentifier.Item<>(type));
            }
        }

        return (InstanceIdentifier<T>) InstanceIdentifier.create(instanceIdentifier);
    }

}

