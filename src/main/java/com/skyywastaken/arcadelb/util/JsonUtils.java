package com.skyywastaken.arcadelb.util;

import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.Iterator;

public class JsonUtils {
    public static int getIntFromPath(JsonObject passedElement, String path) {
        String[] scorePath = path.split("\\.");
        Iterator<String> scorePathIterator = Arrays.stream(scorePath).iterator();
        JsonObject currentJsonObject = passedElement;
        int playerScore = -1;
        while (scorePathIterator.hasNext()) {
            String currentPathPart = scorePathIterator.next();
            if (!scorePathIterator.hasNext()) {
                playerScore = currentJsonObject.get(currentPathPart).getAsInt();
            } else {
                currentJsonObject = currentJsonObject.getAsJsonObject(currentPathPart);
            }
        }
        return playerScore;
    }
}
