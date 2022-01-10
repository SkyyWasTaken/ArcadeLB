package com.skyywastaken.arcadelb.io.hypixel;

import com.skyywastaken.arcadelb.io.hypixel.request.HypixelInfoRequest;
import com.skyywastaken.arcadelb.io.hypixel.request.HypixelKeyCheckRequest;
import com.skyywastaken.arcadelb.util.ConfigManager;

public class HypixelQueryManager {
    private final static HypixelQueryManager instance = new HypixelQueryManager();
    private HypixelKeyCheckRequest lastRequest;
    private int queriesRemaining = 0;

    private HypixelQueryManager() {
        startApiKeyRefresh();
    }

    public static HypixelQueryManager getInstance() {
        return instance;
    }

    public void startApiKeyRefresh() {
        new Thread(this::requestApiKeyInfo).start();
    }

    public synchronized void requestApiKeyInfo() {
        lastRequest = new HypixelKeyCheckRequest(null);
        queriesRemaining = lastRequest.getRemainingUses();
        notifyAll();
    }

    public HypixelKeyCheckRequest getKeyInfo(String key) {
        if (key == null || key.equals(ConfigManager.getAPIKey())) {
            checkKeyInfoValidity();
            return lastRequest;
        }
        HypixelKeyCheckRequest request = new HypixelKeyCheckRequest(key);
        request.requestNow();
        return request;
    }

    private synchronized void checkKeyInfoValidity() {
        if (lastRequest == null) {
            try {
                wait();
            } catch (InterruptedException ignored) {

            }
        }
        if (lastRequest.getRequestTime() / 15000 < System.currentTimeMillis() / 15000) {
            requestApiKeyInfo();
        }
    }

    public int getRemainingQueries() {
        checkKeyInfoValidity();
        return queriesRemaining;
    }

    public boolean isKeyThrottled() {
        checkKeyInfoValidity();
        return getRemainingQueries() == 0;
    }

    public boolean apiKeyIsInvalid() {
        checkKeyInfoValidity();
        return !lastRequest.requestWasSuccessful() && lastRequest.getUnsuccessfulCause().equals("Invalid API key");
    }

    public HypixelInfoRequest requestPlayerInfo(String playerUUID) throws KeyThrottledException {
        runKeyDecrement();
        HypixelInfoRequest request = new HypixelInfoRequest(playerUUID);
        request.requestNow();
        return request;
    }

    private synchronized void runKeyDecrement() throws KeyThrottledException {
        if (getRemainingQueries() == 0) {
            throw new KeyThrottledException();
        }
        queriesRemaining--;
    }
}
