package com.skyywastaken.arcadelb.util.score;

import com.google.gson.JsonObject;

public class HypixelKey {
    private final JsonObject keyResponse;

    public HypixelKey() {
        keyResponse = HypixelQueryHelper.getKeyResponse(null);
    }

    public HypixelKey(String key) {
        keyResponse = HypixelQueryHelper.getKeyResponse(key);
    }

    public boolean isInvalid() {
        return !keyResponse.get("success").getAsBoolean() && keyResponse.get("cause").getAsString().equals("Invalid API key");
    }

    public boolean isThrottled() {
        return keyResponse == null;
    }

    public int getRemainingUses() {
        return 120 - keyResponse.get("record").getAsJsonObject().get("queriesInPastMin").getAsInt();
    }
}
