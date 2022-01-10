package com.skyywastaken.arcadelb.io.hypixel;

import com.google.gson.JsonObject;
import com.skyywastaken.arcadelb.io.hypixel.request.HypixelKeyCheckRequest;
import com.skyywastaken.arcadelb.stats.game.StatType;
import com.skyywastaken.arcadelb.util.io.JsonUtils;
import com.skyywastaken.arcadelb.util.thread.MessageHelper;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.io.IOException;
import java.util.UUID;

public class HypixelQueryHelper {
    public static int getUpdatedScoreByUUID(UUID passedUUID, StatType statBeingTracked) throws IOException {
        JsonObject playerStats;
        try {
            playerStats = HypixelQueryManager.getInstance().requestPlayerInfo(passedUUID.toString()).getResponseJson();
        } catch (KeyThrottledException e) {
            sendThrottleMessage();
            return -1;
        }
        int returnInt = 0;
        for (String currentPath : statBeingTracked.getHypixelPaths()) {
            returnInt += JsonUtils.getIntFromJson(playerStats, currentPath);
        }
        return returnInt;
    }

    public static boolean runKeyCheckWithFeedback(String key) {
        if (key != null) {
            HypixelKeyCheckRequest testKey = HypixelQueryManager.getInstance().getKeyInfo(key);
            if (testKey.keyIsInvalid()) {
                sendInvalidMessage();
                return false;
            }
            return true;
        }
        if (HypixelQueryManager.getInstance().isKeyThrottled()) {
            sendThrottleMessage();
            return true;
        } else if (HypixelQueryManager.getInstance().apiKeyIsInvalid()) {
            sendInvalidMessage();
            return false;
        }
        return true;
    }

    private static void sendThrottleMessage() {
        MessageHelper.sendNullAndThreadSafeMessage(new ChatComponentText(EnumChatFormatting.RED + "Your key is " +
                "being throttled! Make sure you don't have too many apps using your key and don't refresh the " +
                "board too often!"));
    }

    private static void sendInvalidMessage() {
        MessageHelper.sendNullAndThreadSafeMessage(new ChatComponentText(EnumChatFormatting.RED
                + "Your Hypixel API key is invalid! Use '/arcadelb setapikey <key>' to set it! If you need a new " +
                "key, run '/api new' on Hypixel!"));
    }
}
