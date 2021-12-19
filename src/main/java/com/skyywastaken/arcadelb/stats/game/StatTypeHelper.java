package com.skyywastaken.arcadelb.stats.game;

import com.skyywastaken.arcadelb.stats.load.StatTypeHolder;

import java.util.HashMap;
import java.util.Set;

public class StatTypeHelper {
    private final HashMap<String, StatType> statTypeHashMap = new HashMap<>();

    public boolean statExists(String passedStatString) {
        return this.statTypeHashMap.containsKey(passedStatString);
    }

    public StatType getStatTypeFromString(String passedStatString) {
        return this.statTypeHashMap.get(passedStatString);
    }

    public void registerStats(StatTypeHolder passedStats) {
        for (StatType statType : passedStats.statTypeList) {
            registerStat(statType);
        }
    }

    private void registerStat(StatType passedStatType) {
        if (!this.statTypeHashMap.containsKey(passedStatType.getPlayerFriendlyPath())) {
            this.statTypeHashMap.put(passedStatType.getPlayerFriendlyPath(), passedStatType);
        }
    }

    public Set<String> getAllStats() {
        return this.statTypeHashMap.keySet();
    }
}
