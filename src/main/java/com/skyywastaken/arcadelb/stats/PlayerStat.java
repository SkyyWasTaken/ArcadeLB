package com.skyywastaken.arcadelb.stats;

public class PlayerStat {
    private final String playerName;
    public final boolean isCurrentPlayer;
    private int playerScore;

    public PlayerStat(String passedName, int passedScore, boolean passedIsCurrentPlayer) {
        this.playerName = passedName;
        this.isCurrentPlayer = passedIsCurrentPlayer;
        this.playerScore = passedScore;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlayerScore(int newScore) {
        this.playerScore = newScore;
    }

    public int getPlayerScore() {
        return this.playerScore;
    }
}
