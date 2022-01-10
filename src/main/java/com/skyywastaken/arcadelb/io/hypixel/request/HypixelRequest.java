package com.skyywastaken.arcadelb.io.hypixel.request;

import com.google.gson.JsonObject;
import com.skyywastaken.arcadelb.util.io.ConnectionUtils;

import java.net.HttpURLConnection;

public abstract class HypixelRequest {
    protected boolean requestCompleted = false;
    protected int httpResponseCode;
    protected boolean requestSuccessful;
    protected String unsuccessfulCause;

    public void requestNow() {
        checkRequest();
    }

    public void checkRequest() {
        if (!requestCompleted) {
            JsonObject requestObject = getObject();
            handlePreStuff(requestObject);
            doRequest(requestObject);
        }
    }

    private JsonObject getObject() {
        requestCompleted = true;
        HttpURLConnection connection = ConnectionUtils.getHttpURLConnectionFromString(getRequestURLString());
        return ConnectionUtils.getJsonObjectFromConnection(connection);
    }

    private void handlePreStuff(JsonObject passedObject) {
        if (passedObject == null) {
            setVariablesForJsonFailure();
            return;
        }

        requestSuccessful = passedObject.get("success").getAsBoolean();
        if (!requestSuccessful) {
            unsuccessfulCause = passedObject.get("cause").getAsString();
        }
    }

    public boolean requestWasSuccessful() {
        checkRequest();
        return requestSuccessful;
    }

    public String getUnsuccessfulCause() {
        checkRequest();
        return unsuccessfulCause;
    }

    protected abstract void doRequest(JsonObject passedObject);

    protected abstract String getRequestURLString();

    protected abstract void setVariablesForJsonFailure();
}
