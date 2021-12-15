package com.skyywastaken.arcadelb.leaderboard;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.skyywastaken.arcadelb.stats.game.StatType;
import sun.security.validator.ValidatorException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class VenomHelper {
    public static JsonElement requestLeaderboard(StatType statType) throws ValidatorException {
        URL url;
        try {
            url = new URL("https://cdn.hyarcade.xyz/lb?path=." + statType.getVenomPath());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        try {
            URLConnection request = url.openConnection();
            JsonParser jsonParser = new JsonParser();
            return jsonParser.parse(new InputStreamReader((InputStream) request.getContent()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
