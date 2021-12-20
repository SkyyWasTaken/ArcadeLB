package com.skyywastaken.arcadelb.leaderboard;

import net.minecraft.client.Minecraft;

public class LeaderboardRowInfo {
    public final String FAR_LEFT_TEXT;
    public final String PLAYER_NAME;
    public final String SCORE;

    public final boolean IS_CURRENT_PLAYER;

    public LeaderboardRowInfo(String placeNumber, String playerName, String score, boolean isCurrentPlayer) {
        this.FAR_LEFT_TEXT = placeNumber;
        this.PLAYER_NAME = playerName;
        this.SCORE = score;
        this.IS_CURRENT_PLAYER = isCurrentPlayer;
    }

    public int getFarLeftTextSize() {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.FAR_LEFT_TEXT);
    }

    public int getPlayerNameTextSize() {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.PLAYER_NAME);
    }

    public int getRightTextSize() {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.SCORE);
    }
}
