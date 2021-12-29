package com.skyywastaken.arcadelb.leaderboard.format;

import com.skyywastaken.arcadelb.stats.PlayerStat;
import com.skyywastaken.arcadelb.stats.game.FormatType;
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
        return new LeaderboardRowInfo(noBoardSelectedText, "", "", RowType.INFO);
    }

    static LeaderboardRowInfo getHeaderRow(StatType trackedStat) {
        String headerText = EnumChatFormatting.BOLD + trackedStat.getHeaderText();
        return new LeaderboardRowInfo(headerText, "", "", RowType.HEADER);
    }

    private static LeaderboardRowInfo getFetchRow() {
        String fetchText = "Fetching leaderboard...";
        return new LeaderboardRowInfo(fetchText, "", "", RowType.INFO);
    }

    private static LeaderboardRowInfo getDividerRow() {
        return new LeaderboardRowInfo("...", "", "", RowType.DIVIDER);
    }

    static LeaderboardRowInfo generatePlayerRow(String placeString, PlayerStat playerStat, StatType currentStatType) {
        if (playerStat == null) {
            FormatHelper.triggerUpdate();
            return new LeaderboardRowInfo("Loading your score...", "", "", RowType.INFO);
        }
        String scoreString;
        if (currentStatType.isReversed && playerStat.getPlayerScore() == 0) {
            scoreString = "Unset!";
        } else {
            FormatType formatType = currentStatType.getFormatType();
            if (formatType == null) {
                formatType = FormatType.NORMAL;
            }
            scoreString = formatType.formatScore(playerStat.getPlayerScore());
        }
        return new LeaderboardRowInfo(placeString + ".",
                (playerStat.isCurrentPlayer ? EnumChatFormatting.BOLD + "" : "")
                        + playerStat.getPlayerName(), scoreString,
                playerStat.isCurrentPlayer ? RowType.CURRENT_PLAYER : RowType.OTHER_PLAYER);
    }
}
