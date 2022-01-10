package com.skyywastaken.arcadelb.io.hypixel.request;

import com.google.gson.JsonObject;
import com.skyywastaken.arcadelb.util.ConfigManager;

public class HypixelKeyCheckRequest extends HypixelRequest {
    private final String REQUEST_KEY;
    private boolean isThrottled;
    private int remainingUses;
    private long requestTime;

    public HypixelKeyCheckRequest(String passedKey) {
        this.REQUEST_KEY = getRequestKey(passedKey);
    }

    public int getRemainingUses() {
        checkRequest();
        return remainingUses;
    }

    public long getRequestTime() {
        return requestTime;
    }

    @Override
    protected void doRequest(JsonObject passedObject) {
        requestTime = System.currentTimeMillis();
        if (!requestSuccessful) {
            remainingUses = 0;
        }
        isThrottled = passedObject.get("throttle") != null;

        if (requestSuccessful) {
            setSuccessVariables(passedObject);
        }
    }

    protected void setSuccessVariables(JsonObject keyResponse) {
        JsonObject record = keyResponse.get("record").getAsJsonObject();
        int maxQueries = record.get("limit").getAsInt();
        int queriesUsed = record.get("queriesInPastMin").getAsInt() + 1;
        remainingUses = maxQueries - queriesUsed;
        if (remainingUses == 0) {
            isThrottled = true;
        }
    }

    private String getRequestKey(String passedKey) {
        if (passedKey == null) {
            return ConfigManager.getAPIKey();
        } else {
            return passedKey;
        }
    }

    @Override
    protected String getRequestURLString() {
        String requestKey;
        if (REQUEST_KEY == null) {
            requestKey = ConfigManager.getAPIKey();
        } else {
            requestKey = REQUEST_KEY;
        }
        return "https://api.hypixel.net/key?key=" + requestKey;
    }

    @Override
    protected void setVariablesForJsonFailure() {
        requestSuccessful = false;
        isThrottled = false;
        remainingUses = 0;
        unsuccessfulCause = "Response is not a JSON object!";
    }

    public boolean keyIsInvalid() {
        return !requestWasSuccessful() && getUnsuccessfulCause().equals("Invalid API key");
    }

    public boolean keyIsThrottled() {
        checkRequest();
        return isThrottled;
    }
}
