package com.skyywastaken.arcadelb.stats.game;

import java.util.HashMap;

public class StatTypeHelper {
    private final HashMap<String, StatType> statTypeHashMap = new HashMap<>();

    public StatTypeHelper() {
        registerAllStats();
    }

    private void registerStat(StatType passedStatType) {
        this.statTypeHashMap.put(passedStatType.getPlayerFriendlyPath(), passedStatType);
    }

    private <T extends Enum<T>> void registerStats(Class<T> passedEnum) {
        for (T currentValue : passedEnum.getEnumConstants()) {
            if (currentValue instanceof StatType) {
                registerStat((StatType) currentValue);
            }
        }
    }

    public boolean statExists(String passedStatString) {
        return this.statTypeHashMap.containsKey(passedStatString);
    }

    public StatType getStatTypeFromString(String passedStatString) {
        return this.statTypeHashMap.get(passedStatString);
    }

    private void registerAllStats() {
        registerStats(PartyGames.class);
    }
}
