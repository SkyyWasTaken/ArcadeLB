package com.skyywastaken.arcadelb.stats.statupdater;

import com.skyywastaken.arcadelb.stats.ArcadeLeaderboard;
import com.skyywastaken.arcadelb.stats.PlayerStat;
import com.skyywastaken.arcadelb.util.ConfigManager;
import com.skyywastaken.arcadelb.util.score.HypixelQueryHelper;
import com.skyywastaken.arcadelb.util.thread.MessageHelper;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LeaderboardUpdateHelper extends Thread {
    private final ArcadeLeaderboard LEADERBOARD;

    public LeaderboardUpdateHelper(ArcadeLeaderboard passedLeaderboard) {
        this.LEADERBOARD = passedLeaderboard;
    }

    @Override
    public void run() {
        if (!HypixelQueryHelper.runKeyCheckWithFeedback(ConfigManager.getAPIKey())) {
            return;
        }
        int keyUseAmount = HypixelQueryHelper.getRemainingUsesOnKey();
        if (keyUseAmount < ConfigManager.getTotalTracked() + 1) {
            MessageHelper.sendThreadSafeMessage(new ChatComponentText(EnumChatFormatting.RED + "Your key only has " + keyUseAmount + " uses left this minute! Partially updating the board and stopping."));
        }
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(ConfigManager.getAmountOfPlayersToUpdate(),
                ConfigManager.getAmountOfPlayersToUpdate(), 10, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());
        int i = 1;
        for (Map.Entry<UUID, PlayerStat> currentEntry : this.LEADERBOARD.getLeaderboard().entrySet()) {
            if (i++ >= keyUseAmount) {
                break;
            }
            PlayerUpdateThread newUpdateThread = new PlayerUpdateThread(this.LEADERBOARD.getStatType(),
                    currentEntry.getKey(), this);
            threadPoolExecutor.execute(newUpdateThread);
        }
        threadPoolExecutor.shutdown();
        try {
            threadPoolExecutor.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.LEADERBOARD.sortScores();
    }

    public synchronized void updateScore(UUID playerUUID, int newScore) {
        LEADERBOARD.getLeaderboard().get(playerUUID).setPlayerScore(newScore);
    }

}
