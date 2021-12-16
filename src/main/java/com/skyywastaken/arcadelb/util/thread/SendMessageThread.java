package com.skyywastaken.arcadelb.util.thread;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class SendMessageThread extends Thread {
    private final IChatComponent passedComponent;
    private final MessageHelper passedThreadHelper;
    private final int passedDelay;

    public SendMessageThread(IChatComponent messageToSend, MessageHelper passedThreadHelper, int passedDelay) {
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
        ChatComponentText modNameMessage = new ChatComponentText(EnumChatFormatting.GOLD + ""
                + EnumChatFormatting.BOLD + "ARCADELB> " + EnumChatFormatting.RESET);
        modNameMessage.appendSibling(this.passedComponent);
        Minecraft.getMinecraft().addScheduledTask(() ->
                MessageHelper.sendThreadSafeMessage(modNameMessage));
    }
}
