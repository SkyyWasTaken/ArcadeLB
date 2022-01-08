package com.skyywastaken.arcadelb.util.thread;

import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;

public class SendMessageThread extends Thread {
    private final IChatComponent messageToSend;
    private final MessageHelper messageHelper;
    private final int messageDelay;

    public SendMessageThread(IChatComponent passedMessage, MessageHelper passedMessageHelper, int passedDelay) {
        this.messageToSend = passedMessage;
        this.messageHelper = passedMessageHelper;
        this.messageDelay = passedDelay;
    }

    @Override
    public void run() {
        synchronized (this) {
            try {
                sleep(messageDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (Minecraft.getMinecraft().thePlayer == null) {
            enqueueMessage();
        } else {
            addMessageToMainThread();
        }
    }

    private void enqueueMessage() {
        this.messageHelper.addQueuedMessage(this.messageToSend);
    }

    private void addMessageToMainThread() {
        Minecraft.getMinecraft().addScheduledTask(() -> MessageHelper.sendUnsafeMessage(messageToSend));
    }
}
