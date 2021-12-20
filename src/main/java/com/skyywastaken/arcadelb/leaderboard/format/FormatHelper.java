package com.skyywastaken.arcadelb.leaderboard.format;

import com.skyywastaken.arcadelb.leaderboard.LeaderboardRowInfo;
import com.skyywastaken.arcadelb.stats.PlayerStat;
import com.skyywastaken.arcadelb.stats.game.StatType;
import net.minecraft.util.EnumChatFormatting;

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
        String noBoardSelectedText = EnumChatFormatting.BOLD + "No leaderboard selected!";
        return new LeaderboardRowInfo(noBoardSelectedText, "", "", false);
    }

    static LeaderboardRowInfo getHeaderRow(StatType trackedStat) {
        String headerText = EnumChatFormatting.BOLD + trackedStat.getHeaderText();
        return new LeaderboardRowInfo(headerText, "", "", false);
    }

    private static LeaderboardRowInfo getFetchRow() {
        String fetchText = "Fetching leaderboard...";
        return new LeaderboardRowInfo(fetchText, "", "", false);
    }

    private static LeaderboardRowInfo getDividerRow() {
        return new LeaderboardRowInfo("...", "", "", false);
    }

    static LeaderboardRowInfo generatePlayerRow(String placeString, PlayerStat playerStat) {
        if (playerStat == null) {
            FormatHelper.triggerUpdate();
            return new LeaderboardRowInfo("Loading your score...", "", "", false);
        }
        return new LeaderboardRowInfo(placeString + ".", (playerStat.isCurrentPlayer ? EnumChatFormatting.BOLD + "" : "") + playerStat.getPlayerName(), playerStat.getPlayerScore() + "", playerStat.isCurrentPlayer);
    }
}
