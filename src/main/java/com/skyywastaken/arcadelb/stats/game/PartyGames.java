package com.skyywastaken.arcadelb.stats.game;

import net.minecraft.util.EnumChatFormatting;

public enum PartyGames implements StatType {
    WINS("wins1", "wins_party", "wins", "Party Games Wins", false),
    STARS_EARNED("starsEarned", "total_stars_party", "starsearned",
            "Party Games Stars", false),
    ROUNDS_WON("roundsWon", "round_wins_party", "roundswon",
            "Party Games Round Wins", false),
    ANIMAL_SLAUGHTER_WINS("animalSlaughterWins", "animal_slaughter_round_wins_party",
            "animalslaughter.wins", "Animal Slaughter Wins", false),
    ANIMAL_SLAUGHTER_KILLS("animalSlaughterKills", "animal_slaughter_kills_party",
            "animalslaughter.kills", "Animal Slaughter Kills", false),
    ANIMAL_SLAUGHTER_BEST_SCORE("animalSlaughterPB", "animal_slaughter_best_score_party",
            "animalslaughter.bestscore", "Animal Slaughter PB", false),
    ANVIL_SPLEEF_WINS("anvilSpleefWins", "anvil_spleef_round_wins_party",
            "anvilspleef.wins", "Anvil Spleef Wins", false),
    ANVIL_SPLEEF_BEST_TIME("anvilSpleefPB", "anvil_spleef_best_time_party",
            "anvilspleef.bestime", "Anvil Spleef Time", false),
    BOMBARDMENT_WINS("bombardmentWins", "bombardment_round_wins_party",
            "bombardment.wins", "Bombardment Wins", false),
    BOMBARDMENT_BEST_TIME("bombardmentPB", "bombardment_best_time_party",
            "bombardment.besttime", "Bombardment Best Time", false),
    CHICKEN_RINGS_WINS("chickenRingsWins", "anvil_spleef_round_wins_party",
            "chickenrings.wins", "Chicken Rings Wins", false),
    CHICKEN_RINGS_BEST_TIME("chickenRingsPB", "chicken_rings_best_time_party",
            "chickenrings.besttime", "Chicken Rings Time", true),
    DIVE_WINS("diveWins", "dive_round_wins_party", "dive.wins",
            "Dive Wins", false),
    DIVE_PB("divePB", "dive_best_score_party", "dive.bestscore",
            "Dive PB", false),
    DIVE_TOTAL_SCORE("diveScore", "dive_total_score_party", "dive.totalscore",
            "Dive Total Score", false),
    HIGH_GROUND_WINS("highGroundWins", "high_ground_round_wins_party",
            "highground.wins", "High Ground Wins", false),
    HIGH_GROUND_TOTAL_SCORE("highGroundScore",
            "high_ground_total_score_party", "highground.totalscore",
            "High Ground Total Score", false),
    HIGH_GROUND_BEST_SCORE("highGroundPB",
            "high_ground_best_score_party", "highground.bestscore",
            "High Ground PB", false),
    HOE_HOE_HOE_WINS("hoeWins",
            "hoe_hoe_hoe_round_wins_party", "hoehoehoe.wins", "Hoe Hoe Hoe Wins",
            false),
    HOE_HOE_HOE_SCORE("hoeScore",
            "hoe_hoe_hoe_total_score_party", "hoehoehoe.totalscore",
            "Hoe Hoe Hoe Total Score", false),
    HOE_HOE_HOE_BEST_SCORE("hoePB",
            "hoe_hoe_hoe_best_score_party", "hoehoehoe.bestscore",
            "Hoe Hoe Hoe PB", false),
    JIGSAW_RUSH_BEST_TIME("jigsawPB", "jigsaw_rush_best_time_party",
            "jigsawrush.besttime", "Jigsaw Rush Time", true),
    JUNGLE_JUMP_WINS("jungleJumpWins", "jungle_jump_round_wins_party",
            "junglejump.wins", "Jungle Jump Wins", false),
    JUNGLE_JUMP_BEST_TIME("jungleJumpPB", "jungle_jump_best_time_party",
            "junglejump.besttime", "Jungle Jump Time", true),
    LAB_ESCAPE_WINS("labEscapeWins", "lab_escape_round_wins_party",
            "labescape.wins", "Lab Escape Wins", false),
    LAB_ESCAPE_BEST_TIME("labEscapePB", "lab_escape_best_time_party",
            "labescape.besttime", "Lab Escape Time", true),
    LAWN_MOOWER_WINS("lawnMoowerWins", "lawn_moower_round_wins_party",
            "lawnmoower.wins", "Lawn Moower Wins", false),
    LAWN_MOOWER_TOTAL_SCORE("lawnMoowerScore", "lawn_moower_mowed_total_score_party",
            "lawnmoower.totalscore", "Lawn Moower Total Score", false),
    LAWN_MOOWER_BEST_SCORE("lawnMoowerPB", "lawn_moower_mowed_best_score_party",
            "lawnmoower.bestscore", "Lawn Moower PB", false),
    MINECART_RACING_WINS("minecartRacingWins", "minecart_racing_round_wins_party",
            "minecartracing.wins", "Minecart Racing Wins", false),
    MINECART_RACING_BEST_TIME("minecartRacingPB", "minecart_racing_best_time_party",
            "minecartracing.besttime", "Minecart Racing Time", true),
    RPG_16_WINS("rpgWins", "rpg_16_round_wins_party", "rpg16.wins",
            "RPG-16 Wins", false),
    RPG_16_KILLS("rpgKills", "rpg_16_kills_party", "rpg16.kills",
            "RPG-16 Total Kills", false),
    RPG_16_BEST_SCORE("rpgPB",
            "rpg_16_kills_best_score_party", "rpg16.bestscore", "RPG-16 PB",
            false),
    SPIDER_MAZE_BEST_TIME("spiderMazePB", "spider_maze_best_time_party",
            "spidermaze.besttime", "Spider Maze Time", true),
    FLOOR_IS_LAVA_WINS("theFloorIsLavaWins",
            "the_floor_is_lava_round_wins_party", "floorislava.wins",
            "The Floor Is Lava Wins", false),
    FLOOR_IS_LAVA_BEST_TIME("theFloorIsLavaPB", "the_floor_is_lava_best_time_party",
            "spidermaze.besttime", "The Floor Is Lava Time", true),
    AVALANCHE_WINS("avalancheWins", "avalanche_round_wins_party",
            "avalanche.wins", "Avalanche Wins", false),
    VOLCANO_WINS("volcanoWins", "volcano_round_wins_party", "volcano.wins",
            "Volcano Wins", false),
    PIG_FISHING_WINS("pigFishingWins", "pig_fishing_round_wins_party",
            "pigfishing.wins", "Pig Fishing Wins", false),
    TRAMPOLINIO_WINS("trampolinioWins", "trampolinio_round_wins_party",
            "trampolinio.wins", "Trampolinio Wins", false),
    PIG_JOUSTING_WINS("pigJoustingWins", "pig_jousting_round_wins_party",
            "pigjousting.wins", "Pig Jousting Wins", false),
    WORKSHOP_WINS("workshopWins", "workshop_round_wins_party",
            "workshop.wins", "Workshop Wins", false),
    SHOOTING_RANGE_WINS("shootingRangeWins", "shooting_range_round_wins_party",
            "shootingrange.wins", "Shooting Range Wins", false),
    FROZEN_FLOOR_WINS("frozenFloorWins", "frozen_floor_round_wins_party",
            "frozenfloor.wins", "Frozen Floor Wins", false),
    CANNON_PAINTING_WINS("cannonPaintingWins", "cannon_painting_round_wins_party",
            "cannonpainting.wins", "Cannon Painting Wins", false),
    FIRE_LEAPERS_WINS("fireLeapersWins", "fire_leapers_round_wins_party",
            "fireleapers.wins", "Fire Leapers Wins", false),
    SUPER_SHEEP_WINS("superSheepWins", "super_sheep_round_wins_party",
            "supersheep.wins", "Super Sheep Wins", false);
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
