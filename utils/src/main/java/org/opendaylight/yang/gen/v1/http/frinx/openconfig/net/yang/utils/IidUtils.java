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

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.Identifiable;
import org.opendaylight.yangtools.yang.binding.Identifier;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

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

