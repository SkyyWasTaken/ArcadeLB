package com.skyywastaken.arcadelb.util.thread;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MessageThreadListener {
    private final MessageHelper parentThread;

    public MessageThreadListener(MessageHelper parentThread) {
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
