package com.skyywastaken.arcadelb.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skyywastaken.arcadelb.util.thread.MessageHelper;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Iterator;

public class JsonUtils {
    private static long lastKeyNotification = 0;

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

    public static JsonElement getJsonElementFromURL(URL passedURL) {
        HttpURLConnection request = getURLConnectionFromURL(passedURL);
        if (request == null) {
            return null;
        }
        int response = getResponseCode(request);
        sendFeedbackByResponseCode(response, passedURL);
        if (response != 200) {
            return null;
        }
        InputStreamReader streamReader = getInputStreamReaderFromURLConnection(request);
        if (streamReader == null) {
            return null;
        }
        return new JsonParser().parse(streamReader);
    }

    private static HttpURLConnection getURLConnectionFromURL(URL passedURL) {
        try {
            HttpURLConnection connection = (HttpURLConnection) passedURL.openConnection();
            connection.setRequestMethod("GET");
            return connection;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int getResponseCode(HttpURLConnection connection) {
        try {
            return connection.getResponseCode();
        } catch (IOException e) {
            return 404;
        }
    }

    private static void sendFeedbackByResponseCode(int code, URL sourceURL) {
        if (code == 404) {
            MessageHelper.sendThreadSafeMessage(new ChatComponentText(EnumChatFormatting.RED + "Error 404! Couldn't connect to " + sourceURL.toString()));
        } else if (code == 429) {
            if (System.currentTimeMillis() - lastKeyNotification >= 5000) {
                MessageHelper.sendThreadSafeMessage(new ChatComponentText(EnumChatFormatting.RED + "Your API key has been maxed out! Is it being used for something else?"));
            }
            lastKeyNotification = System.currentTimeMillis();
        }
    }

    private static InputStreamReader getInputStreamReaderFromURLConnection(URLConnection passedConnection) {
        try {
            return new InputStreamReader(passedConnection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
