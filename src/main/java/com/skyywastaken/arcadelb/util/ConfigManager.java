package com.skyywastaken.arcadelb.util;

import com.skyywastaken.arcadelb.ArcadeLB;
import com.skyywastaken.arcadelb.leaderboard.format.FormatHelper;
import net.minecraftforge.common.config.Property;

public class ConfigManager {
    public static double getXOffset() {
        return getXOffsetProperty().getDouble();
    }

    public static double getYOffset() {
        return getYOffsetProperty().getDouble();
    }

    public static int getTotalTracked() {
        return getTotalTrackedProperty().getInt();
    }

    public static boolean getLeaderboardEnabled() {
        return getLeaderboardEnabledProperty().getBoolean();
    }

    public static int getOpacity() {
        return getOpacityProperty().getInt();
    }

    public static boolean getDisplayPlayer() {
        return getDisplayPlayerProperty().getBoolean();
    }

    public static int getAmountOfPlayersToUpdate() {
        return getAmountToUpdateAtOnceProperty().getInt();
    }

    public static String getAPIKey() {
        return getAPIKeyProperty().getString();
    }

    public static void setXOffset(float newValue) {
        getXOffsetProperty().set(newValue);
        saveConfig();
    }

    public static void setYOffset(float newValue) {
        getYOffsetProperty().set(newValue);
        saveConfig();
    }

    public static void setLeaderboardEnabled(boolean newValue) {
        getLeaderboardEnabledProperty().set(newValue);
        saveConfig();
    }

    public static void setOpacity(int newOpacity) {
        getOpacityProperty().set(newOpacity);
        saveConfig();
    }

    public static void setDisplayPlayer(boolean newValue) {
        getDisplayPlayerProperty().set(newValue);
        saveConfig();
        FormatHelper.triggerUpdate();
    }

    public static void setUpdateAmount(int newValue) {
        getAmountToUpdateAtOnceProperty().set(newValue);
        saveConfig();
    }

    public static void setTotalTracked(int newValue) {
        getTotalTrackedProperty().set(newValue);
        saveConfig();
    }

    public static void setAPIKey(String passedKey) {
        getAPIKeyProperty().set(passedKey);
        saveConfig();
    }

    private static Property getXOffsetProperty() {
        return ArcadeLB.configuration.get("main", "xoffset", 0,
                "X position by percentage. Default: 0", 0, 1);
    }

    private static Property getYOffsetProperty() {
        return ArcadeLB.configuration.get("main", "yoffset", .5F,
                "Y position by percentage. Default: .5", 0, 1);
    }

    private static Property getLeaderboardEnabledProperty() {
        return ArcadeLB.configuration.get("main", "leaderboardenabled", true,
                "Whether or not the leaderboard is enabled. Default: true");
    }

    private static Property getOpacityProperty() {
        return ArcadeLB.configuration.get("main", "opacity", 100,
                "The opacity of the leaderboard's background. Default: 100", 0, 255);
    }

    private static Property getTotalTrackedProperty() {
        return ArcadeLB.configuration.get("main", "totaltracked", 50,
                "The amount of players tracked on the leaderboard. Default: 50", 10, 120);
    }

    private static Property getDisplayPlayerProperty() {
        return ArcadeLB.configuration.get("main", "displayplayer", true,
                "Whether or not the leaderboard always displays the current player's score. Default: true");
    }

    private static Property getAmountToUpdateAtOnceProperty() {
        return ArcadeLB.configuration.get("main", "simultaneousUpdateAmount", 5, "The amount of player scores to update at once. "
                + "Higher = faster but more network usage. Default: 5", 1, 120);
    }

    private static Property getAPIKeyProperty() {
        return ArcadeLB.configuration.get("secrets", "apikey", "",
                "Your Hypixel API key. This is used to update the leaderboard");
    }

    private static Property getStatTrackedProperty() {
        return ArcadeLB.configuration.get("main", "selectedstat", "", "Which ");
    }

    private static void saveConfig() {
        ArcadeLB.configuration.save();
    }
}
