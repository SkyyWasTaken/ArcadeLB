package com.skyywastaken.arcadelb.stats;

public class PlayerStat {
    public final boolean isCurrentPlayer;
    private final String playerName;
    private int playerScore;

    public PlayerStat(String passedName, int passedScore, boolean passedIsCurrentPlayer) {
        this.playerName = passedName;
        this.isCurrentPlayer = passedIsCurrentPlayer;
        this.playerScore = passedScore;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public int getPlayerScore() {
        return this.playerScore;
    }

    public void setPlayerScore(int newScore) {
        this.playerScore = newScore;
    }
}
