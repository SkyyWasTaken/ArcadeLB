package com.skyywastaken.arcadelb.leaderboard;

import com.skyywastaken.arcadelb.leaderboard.format.FormatType;
import com.skyywastaken.arcadelb.leaderboard.format.LeaderboardFormatter;
import com.skyywastaken.arcadelb.leaderboard.format.LeaderboardRowInfo;
import com.skyywastaken.arcadelb.leaderboard.format.RowType;
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
            LeaderboardRowInfo currentRow = this.leaderboardFormatter.getLeaderboardRows().get(i);
            renderRow(currentRow, rowTopHeight, rowTopHeight + fontHeight, xOffset, rightBound);
            rowTopHeight = rowTopHeight - fontHeight;
        }
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    private void renderRow(LeaderboardRowInfo rowInfo, int top, int bottom, int left, int right) {
        renderRowRectangle(left, top, right, bottom);
        RowType rowType = rowInfo.ROW_TYPE;
        if (rowType.FORMAT_TYPE == FormatType.CENTERED) {
            renderCenteredText(rowInfo.FAR_LEFT_TEXT, left, right - left, top, rowType.getColor());
        } else {
            renderScore(rowInfo, left, right, top);
        }
    }

    private void renderCenteredText(String text, int baseXOffset, int rowLength, int top, int color) {
        int centeredTextOffset = calculateCenteredTextOffset(text, baseXOffset, rowLength);
        renderText(text, centeredTextOffset, top, color);
    }

    private void renderScore(LeaderboardRowInfo rowInfo, int left, int right, int top) {
        renderText(rowInfo.FAR_LEFT_TEXT, left, top, ConfigManager.getPlaceColor());
        int spacerWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(" ");
        int centerTextOffset = rowInfo.getFarLeftTextSize() + spacerWidth + left;
        renderText(rowInfo.PLAYER_NAME, centerTextOffset, top, rowInfo.ROW_TYPE.getColor());
        int rightTextOffset = right - rowInfo.getRightTextSize();
        renderText(rowInfo.SCORE, rightTextOffset, top, ConfigManager.getScoreColor());
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
