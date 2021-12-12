package com.skyywastaken.arcadelb.leaderboard.format;

import com.skyywastaken.arcadelb.stats.PlayerStat;
import com.skyywastaken.arcadelb.stats.game.StatType;
import net.minecraft.util.EnumChatFormatting;
import com.skyywastaken.arcadelb.leaderboard.LeaderboardRowInfo;

public class FormatHelper {
    public static final LeaderboardRowInfo NO_BOARD_ROW = getNoBoardSelectedRow();
    public static final LeaderboardRowInfo FETCH_ROW = getFetchRow();
    public static final LeaderboardRowInfo DIVIDER_ROW = getDividerRow();

    private static boolean formatNeedsUpdate = false;

    public static void triggerUpdate() {
        formatNeedsUpdate = true;
    }

    static void setUpdated() {
        formatNeedsUpdate = false;
    }

    public static boolean getNeedsUpdate() {
        return formatNeedsUpdate;
    }

    private static LeaderboardRowInfo getNoBoardSelectedRow() {
        String noBoardSelectedText = EnumChatFormatting.BOLD + "" + EnumChatFormatting.RED + "No leaderboard selected!";
        return new LeaderboardRowInfo(noBoardSelectedText, "");
    }

    static LeaderboardRowInfo getHeaderRow(StatType trackedStat) {
        String headerText = EnumChatFormatting.AQUA + "" + EnumChatFormatting.BOLD + trackedStat.getHeaderText();
        return new LeaderboardRowInfo(headerText, "");
    }

    private static LeaderboardRowInfo getFetchRow() {
        String fetchText = EnumChatFormatting.GOLD + "Fetching leaderboard...";
        return new LeaderboardRowInfo(fetchText, "");
    }

    private static LeaderboardRowInfo getDividerRow() {
        String dividerText = EnumChatFormatting.GRAY + "...";
        return new LeaderboardRowInfo(dividerText, "");
    }

    static LeaderboardRowInfo generatePlayerRow(String placeString, PlayerStat playerStat) {
        if (playerStat == null) {
            FormatHelper.triggerUpdate();
            return new LeaderboardRowInfo(EnumChatFormatting.GREEN + "Loading your score...", "");
        }
        String leftText = generateLeftText(placeString, playerStat);
        String rightText = generateRightText(playerStat.getPlayerScore());
        return new LeaderboardRowInfo(leftText, rightText);
    }

    private static String getFormatting(PlayerStat passedPlayerStat) {
        if (passedPlayerStat.isCurrentPlayer) {
            return EnumChatFormatting.GREEN + "" + EnumChatFormatting.BOLD;
        } else {
            return EnumChatFormatting.GOLD + "";
        }
    }

    private static String generateLeftText(String placeNumber, PlayerStat playerStat) {
        return EnumChatFormatting.RED + "" + placeNumber + ". " + getFormatting(playerStat)
                + playerStat.getPlayerName();
    }

    private static String generateRightText(int playerScore) {
        return EnumChatFormatting.LIGHT_PURPLE + "" + playerScore;
    }
}
