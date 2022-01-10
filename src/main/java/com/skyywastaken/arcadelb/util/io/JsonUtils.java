package com.skyywastaken.arcadelb.util.io;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Iterator;

public class JsonUtils {
    private static final long lastKeyNotification = 0;

    public static int getIntFromJson(JsonObject passedElement, String path) {
        String[] scorePath = path.split("\\.");
        Iterator<String> scorePathIterator = Arrays.stream(scorePath).iterator();
        JsonObject currentJsonObject = passedElement;
        while (scorePathIterator.hasNext()) {
            String currentPathPart = scorePathIterator.next();
            if (!scorePathIterator.hasNext()) {
                JsonElement finalObject = currentJsonObject.get(currentPathPart);
                if (finalObject != null) {
                    return currentJsonObject.get(currentPathPart).getAsInt();
                }
            } else {
                currentJsonObject = currentJsonObject.getAsJsonObject(currentPathPart);
            }
        }
        return 0;
    }

    public static JsonElement getVenomElementFromString(String passedAddress) {
        URLConnection request = ConnectionUtils.getHttpURLConnectionFromString(passedAddress);
        if (request == null) {
            return null;
        }
        InputStreamReader streamReader = ConnectionUtils.getInputStreamReaderFromURLConnection(request);
        if (streamReader == null) {
            return null;
        }
        return new JsonParser().parse(streamReader);
    }

    public static JsonElement getJsonElementFromConnection(HttpURLConnection passedConnection) {
        InputStreamReader streamReader = ConnectionUtils.getInputStreamReaderFromURLConnection(passedConnection);
        if (streamReader == null) {
            return null;
        }
        return new JsonParser().parse(streamReader);
    }

}
