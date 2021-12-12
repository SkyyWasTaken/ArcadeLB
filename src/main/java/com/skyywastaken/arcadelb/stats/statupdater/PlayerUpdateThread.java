package com.skyywastaken.arcadelb.stats.statupdater;

import com.skyywastaken.arcadelb.stats.game.StatType;
import com.skyywastaken.arcadelb.util.HypixelQueryHelper;

import java.io.IOException;
import java.util.UUID;

public class PlayerUpdateThread implements Runnable {
    private final StatType CURRENT_STAT_TYPE;
    final UUID PLAYER_UUID;
    final LeaderboardUpdateHelper PARENT_THREAD;

    PlayerUpdateThread(StatType passedCurrentStatType, UUID passedPlayerUUID, LeaderboardUpdateHelper threadToWake) {
        this.CURRENT_STAT_TYPE = passedCurrentStatType;
        this.PLAYER_UUID = passedPlayerUUID;
        this.PARENT_THREAD = threadToWake;
    }

    @Override
    public void run() {
        int newScore;
        try {
            newScore = HypixelQueryHelper.getUpdatedScoreByUUID(PLAYER_UUID, this.CURRENT_STAT_TYPE);
        } catch (IOException e) {
            return;
        }
        PARENT_THREAD.updateScoreAndContinue(PLAYER_UUID, newScore);
    }
}
