package com.skyywastaken.arcadelb.leaderboard.format;

import com.skyywastaken.arcadelb.leaderboard.LeaderboardRowInfo;
import com.skyywastaken.arcadelb.stats.ArcadeLeaderboard;
import com.skyywastaken.arcadelb.stats.PlayerStat;
import com.skyywastaken.arcadelb.stats.game.StatType;
import com.skyywastaken.arcadelb.util.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

public class LeaderboardFormatter {
    private final ArcadeLeaderboard ARCADE_LEADERBOARD;
    private LinkedList<LeaderboardRowInfo> leaderboardRows = new LinkedList<>();

    public LeaderboardFormatter(ArcadeLeaderboard passedLeaderboard) {
        this.ARCADE_LEADERBOARD = passedLeaderboard;
    }

    public LinkedList<LeaderboardRowInfo> getLeaderboardRows() {
        if (FormatHelper.getNeedsUpdate()) {
            FormatHelper.setUpdated();
            this.leaderboardRows = getNewRows();
        }
        return this.leaderboardRows;
    }

    private LinkedList<LeaderboardRowInfo> getNewRows() {
        LinkedList<LeaderboardRowInfo> newRows = new LinkedList<>();
        StatType trackedStat = this.ARCADE_LEADERBOARD.getStatType();
        if (trackedStat == null) {
            newRows.add(FormatHelper.NO_BOARD_ROW);
            return newRows;
        }
        newRows.add(FormatHelper.getHeaderRow(trackedStat));
        if (this.ARCADE_LEADERBOARD.getLeaderboard().size() == 0) {
            newRows.add(FormatHelper.FETCH_ROW);
        } else {
            newRows.addAll(getPlayerRows());
        }

        return newRows;
    }

    private LinkedList<LeaderboardRowInfo> getPlayerRows() {
        if (this.ARCADE_LEADERBOARD.getPlayerInTopTracked() && ConfigManager.getDisplayPlayer()) {
            return getPlayerInvolvedScoreboardRows();
        } else {
            return getNormalScoreboardRows();
        }
    }

    private LinkedList<LeaderboardRowInfo> getPlayerInvolvedScoreboardRows() {
        int playerPlace = 100;
        int playerPlaceIterationCounter = 0;
        for (Map.Entry<UUID, PlayerStat> currentEntry : ARCADE_LEADERBOARD.getLeaderboard().entrySet()) {
            if (currentEntry.getValue().isCurrentPlayer) {
                playerPlace = playerPlaceIterationCounter;
                break;
            }
            playerPlaceIterationCounter++;
        }

        if (playerPlace < 10) {
            return getNormalScoreboardRows();
        }

        LinkedList<LeaderboardRowInfo> splitScoreboardRows = new LinkedList<>(getFirstThreePlayerRows());
        splitScoreboardRows.add(FormatHelper.DIVIDER_ROW);
        splitScoreboardRows.addAll(getPlayerInvolvedRows(playerPlace));
        return splitScoreboardRows;
    }

    private LinkedList<LeaderboardRowInfo> getNormalScoreboardRows() {
        int placeStopPoint;
        if (ConfigManager.getDisplayPlayer()) {
            placeStopPoint = 5;
        } else {
            placeStopPoint = 10;
        }
        LinkedList<LeaderboardRowInfo> returnList
                = new LinkedList<>(getLeaderboardRowsFromIndices(0, placeStopPoint));
        if (ConfigManager.getDisplayPlayer()) {
            returnList.addAll(getUnknownPlayerEnding());
        }
        return returnList;
    }

    private LinkedList<LeaderboardRowInfo> getUnknownPlayerEnding() {
        LinkedList<LeaderboardRowInfo> returnList = new LinkedList<>();
        returnList.add(new LeaderboardRowInfo(EnumChatFormatting.GRAY + "...", ""));
        int trackingLimit = ConfigManager.getTotalTracked();
        returnList.addAll(getLeaderboardRowsFromIndices(trackingLimit - 3, trackingLimit));
        UUID currentPlayerUUID = Minecraft.getMinecraft().getSession().getProfile().getId();
        returnList.add(FormatHelper.generatePlayerRow("??",
                this.ARCADE_LEADERBOARD.getLeaderboard().get(currentPlayerUUID)));
        return returnList;
    }

    private LinkedList<LeaderboardRowInfo> getLeaderboardRowsFromIndices(int start, int end) {
        LinkedList<LeaderboardRowInfo> returnList = new LinkedList<>();
        if (start == 0) {
            int i = 0;
            for (Map.Entry<UUID, PlayerStat> currentEntry : this.ARCADE_LEADERBOARD.getLeaderboard().entrySet()) {
                if (i >= end) {
                    break;
                }
                returnList.add(FormatHelper.generatePlayerRow((++i) + "", currentEntry.getValue()));
            }
        } else {
            LinkedList<PlayerStat> stats =
                    new LinkedList<>(this.ARCADE_LEADERBOARD.getLeaderboard().values());
            for (int i = start; i < end; i++) {
                returnList.add(FormatHelper.generatePlayerRow((i + 1) + "", stats.get(i)));
            }
        }
        return returnList;
    }

    private LinkedList<LeaderboardRowInfo> getPlayerInvolvedRows(int playerPlace) {
        int partialPlaceIterator;
        if (playerPlace > ConfigManager.getTotalTracked() - 3) {
            partialPlaceIterator = ConfigManager.getTotalTracked() - playerPlace;
        } else {
            partialPlaceIterator = 3;
        }

        int searchEnd = partialPlaceIterator + playerPlace;
        return getLeaderboardRowsFromIndices(searchEnd - 6, searchEnd);
    }

    private LinkedList<LeaderboardRowInfo> getFirstThreePlayerRows() {
        return getLeaderboardRowsFromIndices(0, 3);
    }
}
