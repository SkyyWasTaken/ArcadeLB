package com.skyywastaken.arcadelb.stats;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.skyywastaken.arcadelb.ArcadeLB;
import com.skyywastaken.arcadelb.leaderboard.VenomHelper;
import com.skyywastaken.arcadelb.leaderboard.format.FormatHelper;
import com.skyywastaken.arcadelb.stats.game.StatType;
import com.skyywastaken.arcadelb.stats.game.StatTypeHelper;
import com.skyywastaken.arcadelb.stats.statupdater.LeaderboardUpdateHelper;
import com.skyywastaken.arcadelb.util.ConfigManager;
import com.skyywastaken.arcadelb.util.HypixelQueryHelper;
import com.skyywastaken.arcadelb.util.thread.MessageHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;

import java.io.IOException;
import java.util.Arrays;
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
    private boolean playerIsOnLeaderboard = false;
    private boolean boardIsSwitching = false;

    public ArcadeLeaderboard(StatTypeHelper statTypeHelper) {
        this.STAT_TYPE_HELPER = statTypeHelper;
    }

    public void loadLeaderboardFromConfig() {
        String configStatString = ConfigManager.getTrackedStat();
        if (!this.STAT_TYPE_HELPER.statExists(configStatString)) {
            MessageHelper.sendThreadSafeMessage(new ChatComponentText(EnumChatFormatting.RED
                    + "Looks like you don't have a leaderboard selected. Type /arcadelb setboard <args> to fix that!\n"
                    + EnumChatFormatting.GOLD + "Tip: Use tab completion to see available completions!"));
        } else {
            new Thread(() -> setLeaderboardFromStatType(this.STAT_TYPE_HELPER.getStatTypeFromString(configStatString)))
                    .start();
        }
    }

    public void setLeaderboardFromStatType(StatType passedStatType) {
        prepareForStatChange();
        this.statType = passedStatType;
        FormatHelper.triggerUpdate();
        JsonElement venomElement;
        try {
            venomElement = VenomHelper.requestLeaderboard(passedStatType, passedStatType.isReversed());
        } catch (sun.security.validator.ValidatorException e) {
            MessageHelper.sendThreadSafeMessage(new ChatComponentText(EnumChatFormatting.RED + "It seems you're " +
                    "using an out-of-date version of Java! Make sure you have Java 8 installed and check that the " +
                    "launcher is not using the built-in Java as the mod cannot connect to the internet with the " +
                    "bundled Java version."));
            this.statType = null;
            FormatHelper.triggerUpdate();
            return;
        }
        if (venomElement == null) {
            boardIsSwitching = false;
            return;
        }
        HashMap<UUID, PlayerStat> newLeaderboard = parseVenomJson(venomElement);
        boolean currentPlayerIsOnNewBoard = currentPlayerIsOnLeaderboard(newLeaderboard);
        if (!currentPlayerIsOnNewBoard) {
            addCurrentPlayerScoreToMap(newLeaderboard);
        }
        this.leaderboard.putAll(newLeaderboard);
        this.playerIsOnLeaderboard = currentPlayerIsOnNewBoard;
        runFinishingTasks();
    }

    private void runFinishingTasks() {
        this.sortScores();
        FormatHelper.triggerUpdate();
        this.executorService = Executors.newScheduledThreadPool(1);
        this.executorService.scheduleAtFixedRate(this::updateLeaderboard, 1, 1, TimeUnit.MINUTES);
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
        this.leaderboard.clear();
        boardIsSwitching = true;
    }

    private void removeCurrentAutoUpdateInstance() {
        if (executorService != null) {
            this.executorService.shutdownNow();
            executorService = null;
        }
    }

    private LinkedHashMap<UUID, PlayerStat> parseVenomJson(JsonElement passedVenomJson) {
        LinkedHashMap<UUID, PlayerStat> returnMap = new LinkedHashMap<>();
        UUID currentPlayerUUID = Minecraft.getMinecraft().getSession().getProfile().getId();
        int leaderboardLimit = ConfigManager.getTotalTracked();
        int i = 1;
        for (JsonElement currentElement : passedVenomJson.getAsJsonArray()) {
            JsonObject currentObject = currentElement.getAsJsonObject();
            UUID currentObjectUUID = attemptToParseUUID(currentObject);
            if (currentObjectUUID == null) {
                ArcadeLB.getLogger().warn("Failed to parse " + currentPlayerUUID + "'s UUID. Ignoring and continuing.");
                continue;
            }
            boolean thisIsCurrentPlayer = currentObjectUUID.equals(currentPlayerUUID);
            PlayerStat newStat = generatePlayerStatFromJson(currentObject, thisIsCurrentPlayer);
            returnMap.put(currentObjectUUID, newStat);
            if (i >= leaderboardLimit) {
                break;
            }
            i++;
        }
        return returnMap;
    }

    private UUID attemptToParseUUID(JsonObject passedPlayerObject) {
        String uuidString = passedPlayerObject.get("uuidPosix").getAsString();
        try {
            return UUID.fromString(uuidString);
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
        String[] scorePath = this.statType.getVenomPath().split("\\.");
        Iterator<String> scorePathIterator = Arrays.stream(scorePath).iterator();
        JsonObject currentJsonObject = passedObject;
        int playerScore = 0;
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

    public LinkedHashMap<UUID, PlayerStat> getLeaderboard() {
        return leaderboard;
    }

    public void updateLeaderboard() {
        new LeaderboardUpdateHelper(this).start();
    }

    private void addCurrentPlayerScoreToMap(HashMap<UUID, PlayerStat> passedMap) {
        Session currentSession = Minecraft.getMinecraft().getSession();
        UUID currentPlayerUUID = currentSession.getProfile().getId();
        int playerScore;
        if (ConfigManager.getAPIKey().equals("") || !HypixelQueryHelper.isKeyValid(UUID.fromString(ConfigManager.getAPIKey()))) {
            playerScore = 0;
            MessageHelper.sendThreadSafeMessage(new ChatComponentText(EnumChatFormatting.RED
                    + "Your ArcadeLB API key is invalid!"));
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
        entryLinkedList.sort((o1, o2) -> Integer.compare(o2.getValue().getPlayerScore(), o1.getValue().getPlayerScore()));
        LinkedHashMap<UUID, PlayerStat> intermediateLeaderboard = new LinkedHashMap<>();
        for (Map.Entry<UUID, PlayerStat> currentEntry : entryLinkedList) {
            intermediateLeaderboard.put(currentEntry.getKey(), currentEntry.getValue());
        }
        this.leaderboard = intermediateLeaderboard;
        FormatHelper.triggerUpdate();
    }

    public boolean isBoardSwitching() {
        return this.boardIsSwitching;
    }

    public boolean getPlayerInTopTracked() {
        return this.playerIsOnLeaderboard;
    }

    public StatType getStatType() {
        return this.statType;
    }
}
