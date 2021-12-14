package com.skyywastaken.arcadelb.util.thread;

import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;

public class SendMessageThread extends Thread {
    private final IChatComponent passedComponent;
    private final ThreadHelper passedThreadHelper;
    private final int passedDelay;

    public SendMessageThread(IChatComponent messageToSend, ThreadHelper passedThreadHelper, int passedDelay) {
        this.passedComponent = messageToSend;
        this.passedThreadHelper = passedThreadHelper;
        this.passedDelay = passedDelay;
    }

    @Override
    public void run() {
        synchronized (this) {
            try {
                sleep(this.passedDelay); // Trying to avoid the message getting buried
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (Minecraft.getMinecraft().thePlayer == null) {
            this.passedThreadHelper.addQueuedMessage(this.passedComponent);
            return;
        }
        Minecraft.getMinecraft().addScheduledTask(() ->
                Minecraft.getMinecraft().thePlayer.addChatMessage(this.passedComponent));
    }
}
