package com.skyywastaken.arcadelb.stats.game;

import net.minecraft.util.EnumChatFormatting;

public enum PartyGames implements StatType {
    WINS("wins1", "wins_party", "wins", "Party Games Wins", false),
    STARS_EARNED("starsEarned", "total_stars_party", "starsearned",
            "Party Games Stars", false),
    ROUNDS_WON("roundsWon", "round_wins_party", "roundswon",
            "Party Games Round Wins", false),
    ANIMAL_SLAUGHTER_WINS("animalSlaughterWins", "animal_slaughter_round_wins_party",
            "animalslaughterwins", "Animal Slaughter Wins", false),
    ANIMAL_SLAUGHTER_KILLS("animalSlaughterKills", "animal_slaughter_kills_party",
            "animalslaughterkills", "Animal Slaughter Kills", false),
    ANIMAL_SLAUGHTER_BEST_SCORE("animalSlaughterPB", "animal_slaughter_best_score_party",
            "animalslaughterbestscore", "Animal Slaughter PB", false),
    ANVIL_SPLEEF_WINS("anvilSpleefWins", "anvil_spleef_round_wins_party",
            "anvilspleefwins", "Anvil Spleef Wins", false),
    BOMBARDMENT_WINS("bombardmentWins", "bombardment_round_wins_party",
            "bombardmentwins", "Bombardment Wins", false),
    CHICKEN_RINGS_WINS("chickenRingsWins", "anvil_spleef_round_wins_party",
            "chickenringswins", "Chicken Rings Wins", false),
    DIVE_WINS("diveWins", "dive_round_wins_party", "divewins",
            "Dive Wins", false),
    DIVE_PB("divePB", "dive_best_score_party", "divebestscore",
            "Dive PB", false),
    DIVE_TOTAL_SCORE("diveScore", "dive_total_score_party", "divetotalscore",
            "Dive Total Score", false),
    HIGH_GROUND_WINS("highGroundWins", "high_ground_round_wins_party",
            "highgroundwins", "High Ground Wins", false),
    HIGH_GROUND_TOTAL_SCORE("highGroundScore",
            "high_ground_total_score_party", "highgroundtotalscore",
            "High Ground Total Score", false),
    HIGH_GROUND_BEST_SCORE("highGroundPB",
            "high_ground_best_score_party", "highgroundbestscore",
            "High Ground PB", false),
    HOE_HOE_HOE_WINS("hoeWins",
            "hoe_hoe_hoe_round_wins_party", "hoehoehoewins", "Hoe Hoe Hoe Wins", false),
    HOE_HOE_HOE_SCORE("hoeScore",
            "hoe_hoe_hoe_total_score_party", "hoehoehoetotalscore",
            "Hoe Hoe Hoe Total Score", false),
    HOE_HOE_HOE_BEST_SCORE("hoePB",
            "hoe_hoe_hoe_best_score_party",
            "hoehoehoebestscore", "Hoe Hoe Hoe PB", false),
    JUNGLE_JUMP_WINS("jungleJumpWins",
            "jungle_jump_round_wins_party",
            "junglejumpwins", "Jungle Jump Wins", false),
    LAB_ESCAPE_IWNS("labEscapeWins",
            "lab_escape_round_wins_party",
            "labescapewins", "Lab Escape Wins", false),
    LAWN_MOOWER_WINS("lawnMoowerWins",
            "lawn_moower_round_wins_party",
            "lawnmoowerwins", "Lawn Moower Wins", false),
    LAWN_MOOWER_TOTAL_SCORE("lawnMoowerScore",
            "lawn_moower_mowed_total_score_party",
            "lawnmoowertotalscore", "Lawn Moower Total Score", false),
    LAWN_MOOWER_BEST_SCORE("lawnMoowerPB",
            "lawn_moower_mowed_best_score_party",
            "lawnmoowerbestscore", "Lawn Moower PB", false),
    MINECART_RACING_WINS("minecartRacingWins",
            "minecart_racing_round_wins_party",
            "minecartracingwins", "Minecart Racing Wins", false),
    RPG_16_WINS("rpgWins", "rpg_16_round_wins_party",
            "rpg16wins", "RPG-16 Wins", false),
    RPG_16_KILLS("rpgKills", "rpg_16_kills_party",
            "rpg16kills", "RPG-16 Total Kills", false),
    RPG_16_BEST_SCORE("rpgPB",
            "rpg_16_kills_best_score_party", "rpg16bestscore",
            "RPG-16 PB", false),
    FLOOR_IS_LAVA_WINS("theFloorIsLavaWins",
            "the_floor_is_lava_round_wins_party",
            "floorislavawins", "The Floor Is Lava Wins", false),
    AVALANCHE_WINS("avalancheWins",
            "avalanche_round_wins_party",
            "avalanchewins", "Avalanche Wins", false),
    VOLCANO_WINS("volcanoWins",
            "volcano_round_wins_party",
            "volcanowins", "Volcano Wins", false),
    PIG_FISHING_WINS("pigFishingWins",
            "pig_fishing_round_wins_party",
            "pigfishingwins", "Pig Fishing Wins", false),
    TRAMPOLINIO_WINS("trampolinioWins", "trampolinio_round_wins_party",
            "trampoliniowins", "Trampolinio Wins", false),
    PIG_JOUSTING_WINS("pigJoustingWins", "pig_jousting_round_wins_party",
            "pigjoustingwins", "Pig Jousting Wins", false),
    WORKSHOP_WINS("workshopWins", "workshop_round_wins_party", "workshopwins",
            "Workshop Wins", false),
    SHOOTING_RANGE_WINS("shootingRangeWins", "shooting_range_round_wins_party",
            "shootingrangewins", "Shooting Range Wins", false),
    FROZEN_FLOOR_WINS("frozenFloorWins", "frozen_floor_round_wins_party",
            "frozenfloorwins", "Frozen Floor Wins", false),
    CANNON_PAINTING_WINS("cannonPaintingWins", "cannon_painting_round_wins_party",
            "cannonpaintingwins", "Cannon Painting Wins", false),
    FIRE_LEAPERS_WINS("fireLeapersWins", "fire_leapers_round_wins_party",
            "fireleaperswins", "Fire Leapers Wins", false),
    SUPER_SHEEP_WINS("superSheepWins", "super_sheep_round_wins_party",
            "supersheepwins", "Super Sheep Wins", false);
    private final String VENOM_PATH;
    private final String HYPIXEL_PATH;
    private final String HEADER_TEXT;
    private final String FRIENDLY_PATH;
    private final boolean REVERSED;

    PartyGames(String passedVenomPath, String passedHypixelPath, String friendlyPath, String headerText, boolean reversed) {
        this.VENOM_PATH = passedVenomPath;
        this.HYPIXEL_PATH = passedHypixelPath;
        this.HEADER_TEXT = headerText;
        this.FRIENDLY_PATH = friendlyPath;
        this.REVERSED = reversed;
    }


    @Override
    public String getVenomPath() {
        return "partyGames." + this.VENOM_PATH;
    }

    @Override
    public String getHypixelPath() {
        return "player.stats.Arcade." + this.HYPIXEL_PATH;
    }

    @Override
    public String getHeaderText() {
        return EnumChatFormatting.AQUA + "" + EnumChatFormatting.BOLD + this.HEADER_TEXT;
    }

    @Override
    public String getPlayerFriendlyPath() {
        return "partygames." + this.FRIENDLY_PATH;
    }

    @Override
    public boolean isReversed() {
        return this.REVERSED;
    }
}
