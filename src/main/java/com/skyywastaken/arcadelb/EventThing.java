package com.skyywastaken.arcadelb;

import com.skyywastaken.arcadelb.leaderboard.LeaderboardRenderer;
import com.skyywastaken.arcadelb.stats.ArcadeLeaderboard;
import com.skyywastaken.arcadelb.util.ConfigManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventThing {
    private final LeaderboardRenderer renderer;

    public EventThing(ArcadeLeaderboard passedLB) {
        renderer = new LeaderboardRenderer(passedLB);
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        if (!ConfigManager.getLeaderboardEnabled()) {
            return;
        }
        this.renderer.drawScoreboard(event);
    }
}
