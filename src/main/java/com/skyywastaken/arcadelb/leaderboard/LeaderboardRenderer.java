package com.skyywastaken.arcadelb.leaderboard;

import com.skyywastaken.arcadelb.leaderboard.format.LeaderboardFormatter;
import com.skyywastaken.arcadelb.stats.ArcadeLeaderboard;
import com.skyywastaken.arcadelb.util.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.LinkedList;

public class LeaderboardRenderer extends Gui {
    private final LeaderboardFormatter leaderboardFormatter;
    ArcadeLeaderboard leaderboard;


    public LeaderboardRenderer(ArcadeLeaderboard leaderboard) {
        this.leaderboardFormatter = new LeaderboardFormatter(leaderboard);
        this.leaderboard = leaderboard;
    }

    public void drawScoreboard(RenderGameOverlayEvent e) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();
        float newScale = (float) ConfigManager.getLeaderboardScale();
        GlStateManager.scale(newScale, newScale, newScale);
        int scoreboardBottomYOffset = getYOffset((int) (e.resolution.getScaledHeight() / newScale));
        int screenWidth = (int) (e.resolution.getScaledWidth() / newScale);
        int rowLength = getRequiredRowLength(this.leaderboardFormatter.getLeaderboardRows());
        int xOffset = getXOffset(screenWidth, rowLength);

        int rightBound = getRightBound(xOffset, screenWidth, rowLength);
        if (rightBound == screenWidth) {
            xOffset = rightBound - rowLength;
        }

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        int fontHeight = fontRenderer.FONT_HEIGHT;
        int rowTopHeight = scoreboardBottomYOffset - fontHeight;
        for (int i = this.leaderboardFormatter.getLeaderboardRows().size() - 1; i >= 0; i--) {
            renderRowRectangle(xOffset, rowTopHeight, rightBound, rowTopHeight + fontHeight);
            LeaderboardRowInfo currentRow = this.leaderboardFormatter.getLeaderboardRows().get(i);
            if (currentRow.SCORE.equals("")) {
                int centeredTextOffset = calculateCenteredTextOffset(currentRow.FAR_LEFT_TEXT, xOffset, rowLength);
                int color;
                if (i == 0) {
                    color = ConfigManager.getHeaderColor();
                } else {
                    color = ConfigManager.getMiscColor();
                }
                renderText(currentRow.FAR_LEFT_TEXT, centeredTextOffset, rowTopHeight, color);
            } else {
                renderText(currentRow.FAR_LEFT_TEXT, xOffset, rowTopHeight, ConfigManager.getPlaceColor());
                int centerTextOffset = currentRow.getFarLeftTextSize() + fontRenderer.getStringWidth(" ") + xOffset;
                int playerNameColor;
                if (currentRow.IS_CURRENT_PLAYER) {
                    playerNameColor = ConfigManager.getYourNameColor();
                } else {
                    playerNameColor = ConfigManager.getOthersNameColor();
                }
                renderText(currentRow.PLAYER_NAME, centerTextOffset, rowTopHeight, playerNameColor);
                int rightTextOffset = rightBound - currentRow.getRightTextSize();
                renderText(currentRow.SCORE, rightTextOffset, rowTopHeight, ConfigManager.getScoreColor());
            }
            rowTopHeight = rowTopHeight - fontHeight;
        }
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    private int getYOffset(int screenHeight) {
        int fontHeight = Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
        int scoreboardHeight = fontHeight * (this.leaderboardFormatter.getLeaderboardRows().size());
        int scoreboardBottomYOffset = (int) (ConfigManager.getYOffset() * screenHeight + scoreboardHeight / 2);
        if (scoreboardBottomYOffset < scoreboardHeight) {
            scoreboardBottomYOffset = scoreboardHeight;
        } else if (scoreboardBottomYOffset > screenHeight) {
            scoreboardBottomYOffset = screenHeight;
        }
        return scoreboardBottomYOffset;
    }

    private int getXOffset(int screenWidth, int rowLength) {
        int xOffset = (int) (ConfigManager.getXOffset() * screenWidth - rowLength / 2);
        if (xOffset < 0) {
            xOffset = 0;
        }
        return xOffset;
    }

    private int getRightBound(int xOffset, int screenWidth, int rowLength) {
        int rightBound = xOffset + rowLength;
        if (rightBound > screenWidth) {
            rightBound = screenWidth;
        }
        return rightBound;
    }

    private void renderRowRectangle(int left, int top, int right, int bottom) {
        drawRect(left, top, right, bottom, ConfigManager.getOpacity() << 24);
    }

    private void renderText(String text, int xValue, int yValue, int color) {
        FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
        drawString(renderer, text, xValue, yValue, color);
    }

    private int calculateCenteredTextOffset(String text, int xOffset, int rowLength) {
        FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
        return xOffset + (rowLength - renderer.getStringWidth(text)) / 2;
    }

    private int getRequiredRowLength(LinkedList<LeaderboardRowInfo> rowsToCheck) {
        int longestPlaceLength = 0;
        int longestPlayerNameLength = 0;
        int longestScoreLength = 0;
        int longestIndividualRowLength = 0;
        for (LeaderboardRowInfo currentRow : rowsToCheck) {
            if (currentRow.SCORE.equals("")) {
                longestIndividualRowLength = Math.max(currentRow.getFarLeftTextSize(), longestIndividualRowLength);
                continue;
            }
            longestPlaceLength = Math.max(longestPlaceLength, currentRow.getFarLeftTextSize());
            longestPlayerNameLength = Math.max(longestPlayerNameLength, currentRow.getPlayerNameTextSize());
            longestScoreLength = Math.max(longestScoreLength, currentRow.getRightTextSize());
        }
        int totalExtraSpaceInBoard = Minecraft.getMinecraft().fontRendererObj.getStringWidth("   ");
        int longestEntryLength = longestPlaceLength + longestPlayerNameLength + totalExtraSpaceInBoard + longestScoreLength;
        return Math.max(longestIndividualRowLength, longestEntryLength);
    }
}
