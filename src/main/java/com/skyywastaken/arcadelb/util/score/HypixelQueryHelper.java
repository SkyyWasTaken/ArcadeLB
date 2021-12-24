package com.skyywastaken.arcadelb.util.score;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skyywastaken.arcadelb.stats.game.StatType;
import com.skyywastaken.arcadelb.util.ConfigManager;
import com.skyywastaken.arcadelb.util.thread.MessageHelper;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

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
        JsonObject startElement = jsonParser.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject();
        JsonObject currentElement = null;
        int returnInt = 0;
        for (String currentPath : statBeingTracked.getHypixelPaths()) {
            String[] returnPath = currentPath.split("\\.");
            for (String key : returnPath) {
                if (key.equals(returnPath[0])) {
                    JsonElement jsonElement = startElement.get(key);
                    if (jsonElement == null) {
                        return 0;
                    } else {
                        currentElement = jsonElement.getAsJsonObject();
                    }
                } else if (key.equals(returnPath[returnPath.length - 1])) {
                    JsonElement possibleScore = currentElement.get(key);
                    if (possibleScore == null) {
                        continue;
                    }
                    returnInt += possibleScore.getAsInt();
                } else {
                    JsonElement jsonElement = currentElement.get(key);
                    if (jsonElement == null) {
                        return 0;
                    } else {
                        currentElement = jsonElement.getAsJsonObject();
                    }
                }
            }
        }
        return returnInt;
    }

    public static boolean runKeyCheckWithFeedback(UUID passedKey) {
        boolean keyIsValid = isKeyValid(passedKey);
        if (!keyIsValid) {
            MessageHelper.sendThreadSafeMessage(new ChatComponentText(EnumChatFormatting.RED
                    + "Your Hypixel API key is invalid! Use '/api new' to generate a new key and save it using "
                    + "'/arcadelb setapikey <key>'"));
        }
        return keyIsValid;
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