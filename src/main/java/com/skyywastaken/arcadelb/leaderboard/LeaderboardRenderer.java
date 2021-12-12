package com.skyywastaken.arcadelb.leaderboard;

import com.skyywastaken.arcadelb.leaderboard.format.LeaderboardFormatter;
import com.skyywastaken.arcadelb.stats.ArcadeLeaderboard;
import com.skyywastaken.arcadelb.util.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
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
        int scoreboardBottomYOffset = getYOffset(e.resolution.getScaledHeight());
        int screenWidth = e.resolution.getScaledWidth();
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
            if (currentRow.RIGHT_TEXT.equals("")) {
                int centeredTextOffset = calculateCenteredTextOffset(currentRow.LEFT_TEXT, xOffset, rowLength);
                renderText(currentRow.LEFT_TEXT, centeredTextOffset, rowTopHeight);
            } else {
                renderText(currentRow.LEFT_TEXT, xOffset, rowTopHeight);
                int rightTextOffset = rightBound - currentRow.getRightTextSize();
                renderText(currentRow.RIGHT_TEXT, rightTextOffset, rowTopHeight);
            }
            rowTopHeight = rowTopHeight - fontHeight;
        }
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

    private void renderText(String text, int xValue, int yValue) {
        FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
        drawString(renderer, text, xValue, yValue, 0XFFFFFFFF);
    }

    private int calculateCenteredTextOffset(String text, int xOffset, int rowLength) {
        FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
        return xOffset + (rowLength - renderer.getStringWidth(text)) / 2;
    }

    private int getRequiredRowLength(LinkedList<LeaderboardRowInfo> rowsToCheck) {
        int longestRowLength = 0;
        int longestScoreLength = 0;
        int longestIndividualRowLength = 0;
        for (LeaderboardRowInfo currentRow : rowsToCheck) {
            if (currentRow.RIGHT_TEXT.equals("")) {
                longestIndividualRowLength = Math.max(currentRow.getLeftTextSize(), longestIndividualRowLength);
                continue;
            }
            longestRowLength = Math.max(longestRowLength, currentRow.getLeftTextSize());
            longestScoreLength = Math.max(longestScoreLength, currentRow.getRightTextSize());
        }
        int spacerSize = Minecraft.getMinecraft().fontRendererObj.getStringWidth("  ");
        int longestEntryLength = longestRowLength + spacerSize + longestScoreLength;
        return Math.max(longestIndividualRowLength, longestEntryLength);
    }
}
