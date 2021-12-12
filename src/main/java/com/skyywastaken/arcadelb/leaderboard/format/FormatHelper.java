package com.skyywastaken.arcadelb.leaderboard.format;

import com.skyywastaken.arcadelb.stats.PlayerStat;
import net.minecraft.util.EnumChatFormatting;
import com.skyywastaken.arcadelb.leaderboard.LeaderboardRowInfo;

public class FormatHelper {
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

    static LeaderboardRowInfo generatePlayerRow(String placeNumber, PlayerStat playerStat) {
        String playerFormatting;
        if (playerStat == null) {
            FormatHelper.triggerUpdate();
            return new LeaderboardRowInfo(EnumChatFormatting.GREEN + "Loading your score...", "");
        }
        if (playerStat.isCurrentPlayer) {
            playerFormatting = EnumChatFormatting.GREEN + "" + EnumChatFormatting.BOLD;
        } else {
            playerFormatting = EnumChatFormatting.GOLD + "";
        }
        String leftText = EnumChatFormatting.RED + placeNumber + ". " + playerFormatting + playerStat.getPlayerName();
        String rightText = EnumChatFormatting.LIGHT_PURPLE + "" + playerStat.getPlayerScore();
        return new LeaderboardRowInfo(leftText, rightText);
    }
}
