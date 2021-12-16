package com.skyywastaken.arcadelb.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skyywastaken.arcadelb.stats.game.StatType;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class HypixelQueryHelper {
    public static int getUpdatedScoreByUUID(UUID passedUUID, StatType statBeingTracked) throws IOException {
        URL url = new URL("https://api.hypixel.net/player?key=" + ConfigManager.getAPIKey() + "&uuid="
                + passedUUID.toString());

        URLConnection request = url.openConnection();
        JsonParser jsonParser = new JsonParser();
        JsonObject currentElement = jsonParser.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject();
        String[] path = statBeingTracked.getHypixelPath().split("\\.");
        for (String key : path) {

            if (key.equals(path[path.length - 1])) {
                JsonElement possibleScore = currentElement.get(key);
                if (possibleScore == null) {
                    return 0;
                }
                return possibleScore.getAsInt();
            } else {
                JsonElement jsonElement = currentElement.get(key);
                if (jsonElement == null) {
                    return 0;
                } else {
                    currentElement = jsonElement.getAsJsonObject();
                }
            }
        }
        return currentElement.getAsInt();
    }

    public static boolean isKeyValid(UUID passedKey) {
        try {
            URL url = new URL("https://api.hypixel.net/key?key=" + passedKey.toString());
            URLConnection request = url.openConnection();
            JsonParser jsonParser = new JsonParser();
            JsonObject currentElement = jsonParser.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject();
            return currentElement.get("success").getAsBoolean();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
