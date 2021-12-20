package com.skyywastaken.arcadelb.util;

import com.skyywastaken.arcadelb.ArcadeLB;
import com.skyywastaken.arcadelb.leaderboard.format.FormatHelper;
import com.skyywastaken.arcadelb.stats.game.StatType;
import net.minecraftforge.common.config.Property;

public class ConfigManager {
    public static double getXOffset() {
        return getXOffsetProperty().getDouble();
    }

    public static void setXOffset(double newValue) {
        getXOffsetProperty().set(newValue);
        saveConfig();
    }

    public static double getYOffset() {
        return getYOffsetProperty().getDouble();
    }

    public static void setYOffset(float newValue) {
        getYOffsetProperty().set(newValue);
        saveConfig();
    }

    public static int getTotalTracked() {
        return getTotalTrackedProperty().getInt();
    }

    public static void setTotalTracked(int newValue) {
        getTotalTrackedProperty().set(newValue);
        saveConfig();
    }

    public static boolean getLeaderboardEnabled() {
        return getLeaderboardEnabledProperty().getBoolean();
    }

    public static void setLeaderboardEnabled(boolean newValue) {
        getLeaderboardEnabledProperty().set(newValue);
        saveConfig();
    }

    public static int getOpacity() {
        return getOpacityProperty().getInt();
    }

    public static void setOpacity(int newOpacity) {
        getOpacityProperty().set(newOpacity);
        saveConfig();
    }

    public static boolean getDisplayPlayer() {
        return getDisplayPlayerProperty().getBoolean();
    }

    public static void setDisplayPlayer(boolean newValue) {
        getDisplayPlayerProperty().set(newValue);
        saveConfig();
        FormatHelper.triggerUpdate();
    }

    public static int getAmountOfPlayersToUpdate() {
        return getAmountToUpdateAtOnceProperty().getInt();
    }

    public static String getAPIKey() {
        return getAPIKeyProperty().getString();
    }

    public static void setAPIKey(String passedKey) {
        getAPIKeyProperty().set(passedKey);
        saveConfig();
    }

    public static String getTrackedStat() {
        return getStatTrackedProperty().getString();
    }

    public static void setUpdateAmount(int newValue) {
        getAmountToUpdateAtOnceProperty().set(newValue);
        saveConfig();
    }

    public static void setStatTracked(StatType passedStatType) {
        getStatTrackedProperty().set(passedStatType.getPlayerFriendlyPath());
        saveConfig();
    }

    public static int getYourNameColor() {
        return getYourNameColorProperty().getInt();
    }

    public static void setYourNameColor(int newColor) {
        getYourNameColorProperty().set(newColor);
        saveConfig();
    }

    public static int getOthersNameColor() {
        return getOthersNameColorProperty().getInt();
    }

    public static void setOthersNameColor(int newColor) {
        getOthersNameColorProperty().set(newColor);
        saveConfig();
    }

    public static int getHeaderColor() {
        return getHeaderColorProperty().getInt();
    }

    public static void setHeaderColor(int newColor) {
        getHeaderColorProperty().set(newColor);
        saveConfig();
    }

    public static int getScoreColor() {
        return getScoreColorProperty().getInt();
    }

    public static void setScoreColor(int newColor) {
        getScoreColorProperty().set(newColor);
        saveConfig();
    }

    public static int getPlaceColor() {
        return getPlaceColorProperty().getInt();
    }

    public static void setPlaceColor(int newColor) {
        getPlaceColorProperty().set(newColor);
        saveConfig();
    }

    public static int getMiscColor() {
        return getMiscColorProperty().getInt();
    }

    public static void setMiscColor(int newColor) {
        getMiscColorProperty().set(newColor);
        saveConfig();
    }

    public static double getLeaderboardScale() {
        return getBoardScaleProperty().getDouble();
    }

    public static void setLeaderboardScale(float newScale) {
        getBoardScaleProperty().set(newScale);
        saveConfig();
    }

    private static Property getXOffsetProperty() {
        return ArcadeLB.configuration.get("main", "xoffset", 0F,
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
        return ArcadeLB.configuration.get("main", "selectedstat", "", "");
    }

    private static Property getYourNameColorProperty() {
        return ArcadeLB.configuration.get("color", "yournamecolor", 0xFF55FF55,
                "The color of your name on the leaderboard", 0, 0xFFFFFFFF);
    }

    private static Property getOthersNameColorProperty() {
        return ArcadeLB.configuration.get("color", "othersnamecolors", 0xFFFFAA00,
                "The color of other players' names on the leaderboard", 0, 0xFFFFFFFF);
    }

    private static Property getHeaderColorProperty() {
        return ArcadeLB.configuration.get("color", "headercolor", 0xFF55FFFF,
                "The color of the header text", 0, 0xFFFFFFFF);
    }

    private static Property getPlaceColorProperty() {
        return ArcadeLB.configuration.get("color", "placecolor", 0xFFFF5555,
                "The color of the place text. ex. '1.'", 0, 0xFFFFFFFF);
    }

    private static Property getScoreColorProperty() {
        return ArcadeLB.configuration.get("color", "scorecolor", 0xFFFF55FF,
                "The color of the score text", 0, 0xFFFFFFFF);
    }

    private static Property getMiscColorProperty() {
        return ArcadeLB.configuration.get("color", "misccolor", 0xFFAAAAAA,
                "The color of miscellaneous elements like status messages or the divider", 0, 0XFFFFFFFF);
    }

    private static Property getBoardScaleProperty() {
        return ArcadeLB.configuration.get("main", "scale", 1f,
                "The current size of the leaderboard (0.01-50, default 1)", 0.01f, 50f);
    }

    private static void saveConfig() {
        ArcadeLB.configuration.save();
    }
}
