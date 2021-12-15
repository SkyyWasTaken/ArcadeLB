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
            new Thread(() -> {
                setLeaderboardFromVenomJson(this.STAT_TYPE_HELPER.getStatTypeFromString(configStatString));
            }).start();
        }
    }

    public void setLeaderboardFromVenomJson(StatType passedStatType) {
        this.leaderboard.clear();
        boardIsSwitching = true;
        if (executorService != null) {
            this.executorService.shutdownNow();
            executorService = null;
        }
        this.statType = passedStatType;
        FormatHelper.triggerUpdate();
        int leaderboardLimit = ConfigManager.getTotalTracked();
        UUID currentPlayerUUID = Minecraft.getMinecraft().getSession().getProfile().getId();
        boolean playerHasBeenFound = false;
        int i = 1;
        JsonElement venomElement = VenomHelper.requestLeaderboard(passedStatType);
        if (venomElement == null) {
            boardIsSwitching = false;
            return;
        }
        for (JsonElement currentElement : venomElement.getAsJsonArray()) {
            JsonObject currentObject = currentElement.getAsJsonObject();
            UUID uuid;
            try {
                uuid = UUID.fromString(currentObject.get("uuidPosix").getAsString());
            } catch (IllegalArgumentException e) {
                ArcadeLB.getLogger().warn("Failed to parse " + currentPlayerUUID + "'s UUID. Ignoring and continuing.");
                continue;
            }

            JsonObject gameElement = currentObject.get(passedStatType.getVenomPath().split("\\.")[0]).getAsJsonObject();
            boolean thisIsCurrentPlayer = uuid.equals(currentPlayerUUID);
            if (thisIsCurrentPlayer) {
                playerHasBeenFound = true;
            }
            this.leaderboard.put(uuid, new PlayerStat(currentObject.get("name").getAsString(),
                    gameElement.get(passedStatType.getVenomPath().split("\\.")[1]).getAsInt(),
                    thisIsCurrentPlayer));
            if (i >= leaderboardLimit) {
                break;
            }
            i++;
        }
        this.playerIsOnLeaderboard = playerHasBeenFound;
        if (!playerHasBeenFound) {
            addCurrentPlayerScore();
        }
        this.sortScores();
        FormatHelper.triggerUpdate();
        this.executorService = Executors.newScheduledThreadPool(1);
        this.executorService.scheduleAtFixedRate(this::updateLeaderboard, 1, 1, TimeUnit.MINUTES);
        this.boardIsSwitching = false;
    }

    public LinkedHashMap<UUID, PlayerStat> getLeaderboard() {
        return leaderboard;
    }

    public void updateLeaderboard() {
        new LeaderboardUpdateHelper(this).start();
    }

    private void addCurrentPlayerScore() {
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
        this.leaderboard.put(currentPlayerUUID, new PlayerStat(currentSession.getUsername(),
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
