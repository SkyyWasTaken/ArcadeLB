package com.skyywastaken.arcadelb.leaderboard;

import net.minecraft.client.Minecraft;

public class LeaderboardRowInfo {
    public final String LEFT_TEXT;
    public final String RIGHT_TEXT;

    public LeaderboardRowInfo(String leftText, String rightText) {
        this.LEFT_TEXT = leftText;
        this.RIGHT_TEXT = rightText;
    }

    public int getLeftTextSize() {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.LEFT_TEXT);
    }

    public int getRightTextSize() {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.RIGHT_TEXT);
    }
}
