package com.skyywastaken.arcadelb.io.hypixel.request;

import com.google.gson.JsonObject;
import com.skyywastaken.arcadelb.util.ConfigManager;

public class HypixelInfoRequest extends HypixelRequest {
    private final String PLAYER_UUID;
    private JsonObject responseJson;

    public HypixelInfoRequest(String idToRequest) {
        PLAYER_UUID = idToRequest;
    }

    @Override
    protected void doRequest(JsonObject requestObject) {
        responseJson = requestObject;
    }

    @Override
    protected String getRequestURLString() {
        return "https://api.hypixel.net/player?key=" + ConfigManager.getAPIKey() + "&uuid=" + PLAYER_UUID;
    }

    @Override
    protected void setVariablesForJsonFailure() {
        requestSuccessful = false;
        unsuccessfulCause = "Response is not a JSON object!";
    }

    public JsonObject getResponseJson() {
        return responseJson;
    }
}
