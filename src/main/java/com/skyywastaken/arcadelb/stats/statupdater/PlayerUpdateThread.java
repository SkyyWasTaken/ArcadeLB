package com.skyywastaken.arcadelb.stats.statupdater;

import com.skyywastaken.arcadelb.io.hypixel.HypixelQueryHelper;
import com.skyywastaken.arcadelb.stats.game.StatType;

import java.io.IOException;
import java.util.UUID;

public class PlayerUpdateThread implements Runnable {
    final UUID PLAYER_UUID;
    final LeaderboardUpdateHelper updateHelper;
    private final StatType CURRENT_STAT_TYPE;

    PlayerUpdateThread(StatType passedCurrentStatType, UUID passedPlayerUUID, LeaderboardUpdateHelper updateHelper) {
        this.CURRENT_STAT_TYPE = passedCurrentStatType;
        this.PLAYER_UUID = passedPlayerUUID;
        this.updateHelper = updateHelper;
    }

    @Override
    public void run() {
        int newScore;
        try {
            newScore = HypixelQueryHelper.getUpdatedScoreByUUID(PLAYER_UUID, this.CURRENT_STAT_TYPE);
        } catch (IOException e) {
            return;
        }
        if (newScore != -1) {
            updateHelper.updateScore(PLAYER_UUID, newScore);
        }
    }
}
