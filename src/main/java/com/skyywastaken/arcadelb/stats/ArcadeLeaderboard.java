package com.skyywastaken.arcadelb.stats;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.skyywastaken.arcadelb.ArcadeLB;
import com.skyywastaken.arcadelb.io.hypixel.HypixelQueryHelper;
import com.skyywastaken.arcadelb.io.venom.VenomHelper;
import com.skyywastaken.arcadelb.leaderboard.format.FormatHelper;
import com.skyywastaken.arcadelb.stats.game.StatType;
import com.skyywastaken.arcadelb.stats.game.StatTypeHelper;
import com.skyywastaken.arcadelb.stats.statupdater.LeaderboardUpdateHelper;
import com.skyywastaken.arcadelb.util.ConfigManager;
import com.skyywastaken.arcadelb.util.UUIDHelper;
import com.skyywastaken.arcadelb.util.io.JsonUtils;
import com.skyywastaken.arcadelb.util.thread.MessageHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ArcadeLeaderboard {
    private final StatTypeHelper STAT_TYPE_HELPER;
    ScheduledExecutorService executorService = null;
    private LinkedHashMap<UUID, PlayerStat> leaderboard = new LinkedHashMap<>();
    private StatType statType = null;
    private boolean boardIsSwitching = false;

    public ArcadeLeaderboard(StatTypeHelper statTypeHelper) {
        this.STAT_TYPE_HELPER = statTypeHelper;
    }

    public void loadLeaderboardFromConfig() {
        String configStatString = ConfigManager.getTrackedStat();
        if (!this.STAT_TYPE_HELPER.statExists(configStatString)) {
            MessageHelper.sendNullAndThreadSafeMessage(new ChatComponentText(EnumChatFormatting.RED
                    + "Looks like you don't have a leaderboard selected. Type /arcadelb setboard <args> to fix that!\n"
                    + EnumChatFormatting.GOLD + "Tip: Use tab completion to see available completions!"));
        } else {
            new Thread(() -> trySetLeaderboardFromStatType(this.STAT_TYPE_HELPER.getStatTypeFromString(configStatString)))
                    .start();
        }
    }

    public void trySetLeaderboardFromStatType(StatType passedStatType) {
        if (!ConfigManager.getLeaderboardEnabled()) {
            return;
        }
        try {
            setBoardFromStatType(passedStatType);
        } catch (Exception e) {
            e.printStackTrace();
            MessageHelper.sendNullAndThreadSafeMessage(new ChatComponentText("The board couldn't be loaded! Please send your log to the mod author."));
            this.reset();
        }
    }

    private void setBoardFromStatType(StatType passedStatType) {
        prepareForStatChange();
        this.statType = passedStatType;
        FormatHelper.triggerUpdate();
        JsonElement venomElement;
        try {
            venomElement = VenomHelper.requestLeaderboard(passedStatType);
        } catch (sun.security.validator.ValidatorException e) {
            MessageHelper.sendNullAndThreadSafeMessage(new ChatComponentText(EnumChatFormatting.RED + "It seems you're " +
                    "using an out-of-date version of Java! Make sure you have Java 8 installed and check that the " +
                    "launcher is not using the built-in Java as the mod cannot connect to the internet with the " +
                    "bundled Java version."));
            this.reset();
            return;
        }
        if (venomElement == null) {
            boardIsSwitching = false;
            return;
        }
        HashMap<UUID, PlayerStat> newLeaderboard = parseVenomJson(venomElement);
        if (newLeaderboard == null) {
            reset();
            MessageHelper.sendNullAndThreadSafeMessage(new ChatComponentText(EnumChatFormatting.RED + "Looks like " +
                    "something's wrong with the database! Try again later or contact the mod author for help."));
            return;
        }
        boolean currentPlayerIsOnNewBoard = currentPlayerIsOnLeaderboard(newLeaderboard);
        if (!currentPlayerIsOnNewBoard) {
            addCurrentPlayerScoreToMap(newLeaderboard);
        }
        this.leaderboard.putAll(newLeaderboard);
        runFinishingTasks();
        MessageHelper.sendNullAndThreadSafeMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Board loaded!"));
    }

    private void runFinishingTasks() {
        this.sortScores();
        FormatHelper.triggerUpdate();
        this.executorService = Executors.newScheduledThreadPool(1);
        this.executorService.scheduleAtFixedRate(this::updateLeaderboard, 0, 1, TimeUnit.MINUTES);
        this.boardIsSwitching = false;
    }

    private boolean currentPlayerIsOnLeaderboard(HashMap<UUID, PlayerStat> passedMap) {
        for (PlayerStat currentStat : passedMap.values()) {
            if (currentStat.isCurrentPlayer) {
                return true;
            }
        }
        return false;
    }

    private void prepareForStatChange() {
        removeCurrentAutoUpdateInstance();
        reset();
        boardIsSwitching = true;
    }

    private void removeCurrentAutoUpdateInstance() {
        if (executorService != null) {
            this.executorService.shutdownNow();
            executorService = null;
        }
    }

    public void reset() {
        this.statType = null;
        this.boardIsSwitching = false;
        this.leaderboard.clear();
        FormatHelper.triggerUpdate();
    }

    private LinkedHashMap<UUID, PlayerStat> parseVenomJson(JsonElement passedVenomJson) {
        LinkedHashMap<UUID, PlayerStat> returnMap = new LinkedHashMap<>();
        UUID currentPlayerUUID = Minecraft.getMinecraft().getSession().getProfile().getId();
        int leaderboardLimit = ConfigManager.getTotalTracked();
        int i = 1;
        if (!(passedVenomJson instanceof JsonArray)) {
            return null;
        }
        for (JsonElement currentElement : passedVenomJson.getAsJsonArray()) {
            JsonObject currentObject = currentElement.getAsJsonObject();
            UUID currentObjectUUID = attemptToParseUUID(currentObject);
            if (currentObjectUUID == null) {
                ArcadeLB.getLogger().warn("Failed to parse " + currentPlayerUUID + "'s UUID. Ignoring and continuing.");
                continue;
            }
            boolean thisIsCurrentPlayer = currentObjectUUID.equals(currentPlayerUUID);
            PlayerStat newStat;
            try {
                newStat = generatePlayerStatFromJson(currentObject, thisIsCurrentPlayer);
            } catch (NullPointerException e) {
                this.statType = null;
                this.boardIsSwitching = false;
                FormatHelper.triggerUpdate();
                MessageHelper.sendNullAndThreadSafeMessage(new ChatComponentText(EnumChatFormatting.RED + "Looks like the " +
                        "Venom path for the selected leaderboard is borked or I've received an erroneous response from " +
                        "the database! Try loading the board again. If the issue persists, fix the path in its " +
                        "respective json file or contact the mod author."));
                break;
            }
            returnMap.put(currentObjectUUID, newStat);
            if (i >= leaderboardLimit) {
                break;
            }
            i++;
        }
        return returnMap;
    }

    private UUID attemptToParseUUID(JsonObject passedPlayerObject) {
        String uuidString = passedPlayerObject.get("uuid").getAsString();
        try {
            return UUIDHelper.safelyParseUUID(uuidString);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private PlayerStat generatePlayerStatFromJson(JsonObject passedJson, boolean playerIsCurrentPlayer) {
        String playerName = passedJson.get("name").getAsString();
        int playerScore = getPlayerScoreFromJsonObject(passedJson);
        return new PlayerStat(playerName, playerScore, playerIsCurrentPlayer);
    }

    private int getPlayerScoreFromJsonObject(JsonObject passedObject) {
        return JsonUtils.getIntFromJson(passedObject, this.statType.getVenomPath());
    }

    public LinkedHashMap<UUID, PlayerStat> getLeaderboard() {
        return leaderboard;
    }

    public void updateLeaderboard() {
        if (!ConfigManager.getLeaderboardEnabled()) {
            return;
        }
        new LeaderboardUpdateHelper(this).start();
    }

    private void addCurrentPlayerScoreToMap(HashMap<UUID, PlayerStat> passedMap) {
        Session currentSession = Minecraft.getMinecraft().getSession();
        UUID currentPlayerUUID = currentSession.getProfile().getId();
        int playerScore;
        if (!HypixelQueryHelper.runKeyCheckWithFeedback(null)) {
            playerScore = 0;
        } else {
            try {
                playerScore = HypixelQueryHelper.getUpdatedScoreByUUID(currentPlayerUUID, statType);
            } catch (IOException e) {
                new IOException("Could not fetch current player's statistic!", e).printStackTrace();
                return;
            }
        }
        passedMap.put(currentPlayerUUID, new PlayerStat(currentSession.getUsername(),
                playerScore, true));
    }

    public void sortScores() {
        LinkedList<Map.Entry<UUID, PlayerStat>> entryLinkedList = new LinkedList<>(this.leaderboard.entrySet());
        if (!this.statType.isReversed()) {
            entryLinkedList.sort((o1, o2) -> Integer.compare(o2.getValue().getPlayerScore(), o1.getValue().getPlayerScore()));
        } else {
            entryLinkedList.sort(Comparator.comparingInt(o -> o.getValue().getPlayerScore()));
        }
        LinkedHashMap<UUID, PlayerStat> intermediateLeaderboard = new LinkedHashMap<>();
        for (Map.Entry<UUID, PlayerStat> currentEntry : entryLinkedList) {
            intermediateLeaderboard.put(currentEntry.getKey(), currentEntry.getValue());
        }
        Iterator<Map.Entry<UUID, PlayerStat>> intermediateIterator = intermediateLeaderboard.entrySet().iterator();
        HashMap<UUID, PlayerStat> playersToAppend = new HashMap<>();
        while (intermediateIterator.hasNext()) {
            Map.Entry<UUID, PlayerStat> currentEntry = intermediateIterator.next();
            if (currentEntry.getValue().getPlayerScore() == 0) {
                intermediateIterator.remove();
                playersToAppend.put(currentEntry.getKey(), currentEntry.getValue());
            } else {
                break;
            }
        }
        intermediateLeaderboard.putAll(playersToAppend);
        this.leaderboard = intermediateLeaderboard;
        FormatHelper.triggerUpdate();
    }

    public boolean isBoardSwitching() {
        return this.boardIsSwitching;
    }

    public StatType getStatType() {
        return this.statType;
    }
}
