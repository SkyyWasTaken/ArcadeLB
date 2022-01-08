package com.skyywastaken.arcadelb.util.score;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skyywastaken.arcadelb.stats.game.StatType;
import com.skyywastaken.arcadelb.util.ConfigManager;
import com.skyywastaken.arcadelb.util.JsonUtils;
import com.skyywastaken.arcadelb.util.thread.MessageHelper;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class HypixelQueryHelper {
    public static int getUpdatedScoreByUUID(UUID passedUUID, StatType statBeingTracked) throws IOException {
        URL url = new URL("https://api.hypixel.net/player?key=" + ConfigManager.getAPIKey() + "&uuid="
                + passedUUID.toString());

        JsonElement startElement = JsonUtils.getJsonElementFromURL(url);
        if (startElement == null) {
            return -1;
        }
        JsonObject startObject = startElement.getAsJsonObject();
        int returnInt = 0;
        for (String currentPath : statBeingTracked.getHypixelPaths()) {
            returnInt += JsonUtils.getIntFromJson(startObject, currentPath);
        }
        return returnInt;
    }

    public static boolean runKeyCheckWithFeedback(String key) {
        HypixelKey apiKey = new HypixelKey(key);
        if (apiKey.isThrottled()) {
            MessageHelper.sendThreadSafeMessage(new ChatComponentText(EnumChatFormatting.RED + "Your key is " +
                    "being throttled! Make sure you don't have too many apps using your key and don't refresh the " +
                    "board too often!"));
            return true;
        } else if (apiKey.isInvalid()) {
            MessageHelper.sendThreadSafeMessage(new ChatComponentText(EnumChatFormatting.RED
                    + "Your Hypixel API key is invalid! Use '/arcadelb setapikey <key>' to set it! If you need a new " +
                    "key, run '/api new' on Hypixel!"));
            return false;
        }
        return true;
    }

    public static int getRemainingUsesOnKey() {
        HypixelKey apiKey = new HypixelKey();
        return apiKey.getRemainingUses();
    }

    static JsonObject getKeyResponse(String passedKey) {
        String apiKey;
        if (passedKey == null) {
            apiKey = ConfigManager.getAPIKey();
        } else {
            apiKey = passedKey;
        }
        try {
            URL url = new URL("https://api.hypixel.net/key?key=" + apiKey);
            URLConnection request = url.openConnection();
            JsonParser jsonParser = new JsonParser();
            return jsonParser.parse(new InputStreamReader(request.getInputStream())).getAsJsonObject();
        } catch (IOException e) {
            return null;
        }
    }
}
