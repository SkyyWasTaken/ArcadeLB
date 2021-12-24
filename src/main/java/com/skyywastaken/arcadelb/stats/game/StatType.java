package com.skyywastaken.arcadelb.stats.game;

public class StatType {
    public String userFriendlyPath;
    public String venomPath;
    public String[] hypixelPaths;
    public String headerText;
    public boolean isReversed;
    public FormatType formatType;

    public StatType(String userFriendlyPath, String venomPath, String[] hypixelPath, String headerText, boolean isReversed, FormatType passedFormatType) {
        this.userFriendlyPath = userFriendlyPath;
        this.venomPath = venomPath;
        this.hypixelPaths = hypixelPath;
        this.headerText = headerText;
        this.isReversed = isReversed;
        this.formatType = passedFormatType;
    }

    public String getVenomPath() {
        return this.venomPath;
    }

    public String[] getHypixelPaths() {
        return hypixelPaths;
    }

    public String getHeaderText() {
        return headerText;
    }

    public String getPlayerFriendlyPath() {
        return userFriendlyPath;
    }

    public boolean isReversed() {
        return isReversed;
    }

    public FormatType getFormatType() {
        return this.formatType;
    }
}
