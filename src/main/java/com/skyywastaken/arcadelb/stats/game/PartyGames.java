package com.skyywastaken.arcadelb.stats.game;

import net.minecraft.util.EnumChatFormatting;

public enum PartyGames implements StatType {
    WINS("partyGames.wins1", "player.stats.Arcade.wins_party",
            "Party Games Wins", "partygames.wins"),
    STARS_EARNED("partyGames.starsEarned", "player.stats.Arcade.total_stars_party",
            "Party Games Stars", "partygames.starsearned");
    private final String VENOM_PATH;
    private final String HYPIXEL_PATH;
    private final String HEADER_TEXT;
    private final String FRIENDLY_PATH;

    PartyGames(String passedVenomPath, String passedHypixelPath, String headerText, String friendlyPath) {
        this.VENOM_PATH = passedVenomPath;
        this.HYPIXEL_PATH = passedHypixelPath;
        this.HEADER_TEXT = headerText;
        this.FRIENDLY_PATH = friendlyPath;
    }


    @Override
    public String getVenomPath() {
        return this.VENOM_PATH;
    }

    @Override
    public String getHypixelPath() {
        return this.HYPIXEL_PATH;
    }

    @Override
    public String getHeaderText() {
        return EnumChatFormatting.AQUA + "" + EnumChatFormatting.BOLD + this.HEADER_TEXT;
    }

    @Override
    public String getPlayerFriendlyPath() {
        return this.FRIENDLY_PATH;
    }
}
