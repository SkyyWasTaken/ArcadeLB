package com.skyywastaken.arcadelb.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;

public class ThreadHelper {
    public static void sendPlayerMessage(IChatComponent chatComponent) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            try {
                Minecraft.getMinecraft().thePlayer.addChatMessage(chatComponent);
            } catch (NullPointerException e) {
                new Exception("Tried to send a message to the player before the player exists!", e).printStackTrace();
            }
        });
    }
}
