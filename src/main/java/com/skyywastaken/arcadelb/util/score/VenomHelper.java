package com.skyywastaken.arcadelb.util.score;

import com.google.gson.JsonElement;
import com.skyywastaken.arcadelb.stats.game.StatType;
import com.skyywastaken.arcadelb.util.JsonUtils;
import sun.security.validator.ValidatorException;

import java.net.MalformedURLException;
import java.net.URL;


public class VenomHelper {
    public static JsonElement requestLeaderboard(StatType statType) throws ValidatorException {
        URL databaseURL = getLeaderboardURL(statType);
        if (databaseURL == null) {
            return null;
        }
        return JsonUtils.getJsonElementFromURL(databaseURL);
    }

    private static URL getLeaderboardURL(StatType passedStatType) {
        String urlString = getURLString(passedStatType);
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getURLString(StatType passedStatType) {
        String reversedString;
        if (passedStatType.isReversed) {
            reversedString = "&reverse";
        } else {
            reversedString = "";
        }
        return "https://cdn.hyarcade.xyz/lb?min" + reversedString + "&path=." + passedStatType.getVenomPath();
    }
}
