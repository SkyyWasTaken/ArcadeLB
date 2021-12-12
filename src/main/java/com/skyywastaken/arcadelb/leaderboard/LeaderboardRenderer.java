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
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        int fontHeight = fontRenderer.FONT_HEIGHT;
        int screenHeight = e.resolution.getScaledHeight();
        int scoreboardHeight = fontHeight * (this.leaderboardFormatter.getLeaderboardRows().size());
        int scoreboardBottomYOffset = (int) (ConfigManager.getYOffset() * screenHeight + scoreboardHeight / 2);
        if (scoreboardBottomYOffset < scoreboardHeight) {
            scoreboardBottomYOffset = scoreboardHeight;
        } else if (scoreboardBottomYOffset > screenHeight) {
            scoreboardBottomYOffset = screenHeight;
        }

        int screenWidth = e.resolution.getScaledWidth();
        int rowLength = getRequiredRowLength(this.leaderboardFormatter.getLeaderboardRows());
        int xOffset = (int) (ConfigManager.getXOffset() * screenWidth - rowLength / 2);
        if (xOffset < 0) {
            xOffset = 0;
        }
        int rightBound = xOffset + rowLength;
        if (rightBound > screenWidth) {
            xOffset = screenWidth - rowLength;
            rightBound = screenWidth;
        }

        int rowTopHeight = scoreboardBottomYOffset - fontHeight;
        for (int i = this.leaderboardFormatter.getLeaderboardRows().size() - 1; i >= 0; i--) {
            LeaderboardRowInfo currentRow = this.leaderboardFormatter.getLeaderboardRows().get(i);
            drawRect(xOffset, rowTopHeight, rightBound, rowTopHeight + fontHeight, ConfigManager.getOpacity() << 24);
            if (currentRow.RIGHT_TEXT.equals("")) {
                drawString(fontRenderer, currentRow.LEFT_TEXT,
                        xOffset + (rowLength -
                                fontRenderer.getStringWidth(currentRow.LEFT_TEXT)) / 2,
                        rowTopHeight, 0xFFFFFFFF);
            } else {
                drawString(fontRenderer, currentRow.LEFT_TEXT, xOffset, rowTopHeight, 0xFFFFFFFF);
                drawString(fontRenderer, currentRow.RIGHT_TEXT, rightBound - currentRow.getRightTextSize(), rowTopHeight, 0xFFFFFFFF);
            }
            rowTopHeight = rowTopHeight - fontHeight;
        }
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
