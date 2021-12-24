package com.skyywastaken.arcadelb.stats.game;

import com.skyywastaken.arcadelb.stats.load.StatTypeHolder;
import com.skyywastaken.arcadelb.stats.load.StatTypeLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class StatTypeHelper {
    private final HashMap<String, StatType> statTypeHashMap = new HashMap<>();

    public StatTypeHelper() {
        StatTypeLoader loader = new StatTypeLoader();
        registerStatHolders(loader.loadStats());
    }

    public boolean statExists(String passedStatString) {
        return this.statTypeHashMap.containsKey(passedStatString);
    }

    private void registerStatHolders(List<StatTypeHolder> stats) {
        stats.forEach(this::registerStats);
    }

    private void registerStats(StatTypeHolder passedStats) {
        for (StatType statType : passedStats.statTypeList) {
            registerStat(statType);
        }
    }

    public StatType getStatTypeFromString(String passedStatString) {
        return this.statTypeHashMap.get(passedStatString);
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
