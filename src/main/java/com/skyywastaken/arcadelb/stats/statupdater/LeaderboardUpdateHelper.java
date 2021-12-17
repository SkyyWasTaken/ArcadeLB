package com.skyywastaken.arcadelb.stats.statupdater;

import com.skyywastaken.arcadelb.stats.ArcadeLeaderboard;
import com.skyywastaken.arcadelb.stats.PlayerStat;
import com.skyywastaken.arcadelb.util.ConfigManager;
import com.skyywastaken.arcadelb.util.HypixelQueryHelper;
import com.skyywastaken.arcadelb.util.thread.MessageHelper;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
        ExecutorService validKeyCheck = Executors.newSingleThreadExecutor();
        boolean keyIsValid = false;
        try {
            keyIsValid = validKeyCheck.submit(() -> HypixelQueryHelper.isKeyValid(UUID.fromString(ConfigManager.getAPIKey()))).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            validKeyCheck.shutdown();
        } catch (IllegalArgumentException ignored) {
        }
        validKeyCheck.shutdown();
        if (!keyIsValid) {
            MessageHelper.sendThreadSafeMessage(new ChatComponentText(EnumChatFormatting.RED + "Your ArcadeLB API key is invalid!"));
            return;
        }
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(ConfigManager.getAmountOfPlayersToUpdate(),
                ConfigManager.getAmountOfPlayersToUpdate(), 10, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());
        for (Map.Entry<UUID, PlayerStat> currentEntry : this.LEADERBOARD.getLeaderboard().entrySet()) {
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
