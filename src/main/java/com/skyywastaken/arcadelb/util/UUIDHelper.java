package com.skyywastaken.arcadelb.util;

import java.util.UUID;

public class UUIDHelper {
    public static UUID safelyParseUUID(String passedUUIDString) throws IllegalArgumentException {
        try {
            return UUID.fromString(passedUUIDString);
        } catch (IllegalArgumentException ignored) {

        }
        String newUUIDString = passedUUIDString.substring(0, 8) + "-"
                + passedUUIDString.substring(8, 12) + "-"
                + passedUUIDString.substring(12, 16) + "-"
                + passedUUIDString.substring(16, 20) + "-" + passedUUIDString.substring(20);
        return UUID.fromString(newUUIDString);
    }
}
