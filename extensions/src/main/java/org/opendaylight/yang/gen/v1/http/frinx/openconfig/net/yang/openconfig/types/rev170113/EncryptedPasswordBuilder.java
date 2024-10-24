package org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.openconfig.types.rev170113;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The purpose of generated class in src/main/java for Union types is to create new instances of unions from a string representation.
 * In some cases it is very difficult to automate it since there can be unions such as (uint32 - uint16), or (string - uint32).
 *
 * The reason behind putting it under src/main/java is:
 * This class is generated in form of a stub and needs to be finished by the user. This class is generated only once to prevent
 * loss of user code.
 *
 */
public class EncryptedPasswordBuilder {

    private static final String PASSWORD_ENCRYPTED_STRING = "Encrypted[%s]";
    private static final Pattern PASSWORD_ENCRYPTED_PATTERN = Pattern.compile(EncryptedString.PATTERN_CONSTANTS.get(0));

    private EncryptedPasswordBuilder() {

    }

    public static org.opendaylight.yang.gen.v1.http.frinx.openconfig.net.yang.openconfig.types.rev170113.EncryptedPassword getDefaultInstance(java.lang.String defaultValue) {

        return null;
    }

    public static EncryptedPassword getEncryptedPassword(String password) {
        return new EncryptedPassword(new EncryptedString(String.format(PASSWORD_ENCRYPTED_STRING, password)));
    }

    public static String parseEncryptedPassword(EncryptedString encryptString) {
        Matcher matcher = PASSWORD_ENCRYPTED_PATTERN.matcher(encryptString.getValue());
        matcher.matches();
        return matcher.group(1);
    }
}
