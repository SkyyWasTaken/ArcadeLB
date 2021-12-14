package com.skyywastaken.arcadelb.util.thread;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ThreadListeners {
    private final ThreadHelper parentThread;

    public ThreadListeners(ThreadHelper parentThread) {
        this.parentThread = parentThread;
    }

    @SubscribeEvent
    public void onPlayerJoinWorld(EntityJoinWorldEvent e) {
        if (e.entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e.entity;
            if (player.getUniqueID().equals(Minecraft.getMinecraft().getSession().getProfile().getId())) {
                parentThread.sendQueuedMessages();
            }
        }
    }
}
