package com.skyywastaken.arcadelb.io.venom;

import com.google.gson.JsonElement;
import com.skyywastaken.arcadelb.stats.game.StatType;
import com.skyywastaken.arcadelb.util.io.JsonUtils;
import sun.security.validator.ValidatorException;


public class VenomHelper {
    public static JsonElement requestLeaderboard(StatType statType) throws ValidatorException {
        return JsonUtils.getVenomElementFromString(getURLString(statType));
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
