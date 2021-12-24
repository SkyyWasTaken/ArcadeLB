package com.skyywastaken.arcadelb.leaderboard.format;

import net.minecraft.client.Minecraft;

public class LeaderboardRowInfo {
    public final String FAR_LEFT_TEXT;
    public final String PLAYER_NAME;
    public final String SCORE;
    public final RowType ROW_TYPE;

    public LeaderboardRowInfo(String placeNumber, String playerName, String score, RowType passedRowType) {
        this.FAR_LEFT_TEXT = placeNumber;
        this.PLAYER_NAME = playerName;
        this.SCORE = score;
        this.ROW_TYPE = passedRowType;
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
