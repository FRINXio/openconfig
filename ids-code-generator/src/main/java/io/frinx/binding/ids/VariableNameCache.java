/*
 * Copyright © 2017 Frinx and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package io.frinx.binding.ids;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.Iterator;
import java.util.Optional;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.model.api.SchemaPath;

final class VariableNameCache implements AutoCloseable {

    private static final String JOINER = "_";
    private static final int DEFAULT_PARENT_NAME_LENGTH = 2;

    private final BiMap<SchemaPath, String> variableNames = HashBiMap.create();

    String getVariableName(SchemaPath path) {

        // Do some caching
        if (variableNames.containsKey(path)) {
            return variableNames.get(path);
        }

        String constantName = encodeVariableName(path);
        String candidateNoConflictName;
        int nameLength = DEFAULT_PARENT_NAME_LENGTH;

        // Outer loop makes sure there are no collisions in the resulting variable name
        // and if there already is such a name, increase the length of parent name
        do {
            Iterator<QName> iterator = path.getPathTowardsRoot().iterator();
            // Skip target node
            checkArgument(iterator.hasNext());
            iterator.next();

            candidateNoConflictName = getNameWithParents(constantName, nameLength, iterator);
            nameLength++;

            // Just to prevent infinite loop, should not occur
            if (nameLength > 100) {
                throw new IllegalStateException("Unable to generate variable name for " + path);
            }

        } while (variableNames.containsValue(candidateNoConflictName));

        variableNames.put(path, candidateNoConflictName);
        return candidateNoConflictName;
    }

    private String getNameWithParents(String localName, int nameLength, Iterator<QName> iterator) {
        String candidateNoConflictName = localName;
        while (iterator.hasNext()) {
            candidateNoConflictName = encodeVariableName(iterator.next().getLocalName(), false, nameLength)
                    + JOINER + candidateNoConflictName;
        }
        return candidateNoConflictName;
    }

    Optional<String> getExistingVariableName(SchemaPath path) {
        return variableNames.containsKey(path) ? Optional.of(variableNames.get(path)) : Optional.empty();
    }

    private String encodeVariableName(SchemaPath path) {
        String yangName = path.getLastComponent().getLocalName();
        return encodeVariableName(yangName, true, DEFAULT_PARENT_NAME_LENGTH);
    }

    private String encodeVariableName(String yangName, boolean full, int size) {
        String constant = yangName.replaceAll(IdsCodeGenerator.INVALID_CHARS_MATCHER, "").toUpperCase();
        if (full) {
            return constant;
        } else {
            // Shorten the name to first 3 letters
            return constant.length() > size ? constant.substring(0, size) : constant;
        }
    }

    @Override
    public void close() {
        variableNames.clear();
    }
}
