package com.skyywastaken.arcadelb.util.thread;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageHelper {
    private final static MessageHelper mainInstance = new MessageHelper();
    private final ArrayList<IChatComponent> QUEUED_MESSAGES = new ArrayList<>();

    public MessageHelper() {
        MessageThreadListener messageListener = new MessageThreadListener(this);
        MinecraftForge.EVENT_BUS.register(messageListener);
    }

    public static void sendNullAndThreadSafeMessage(IChatComponent chatComponent) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            sendThreadSafeMessage(chatComponent);
        } else {
            mainInstance.QUEUED_MESSAGES.add(chatComponent);
        }
    }

    public void sendQueuedMessages() {
        ExecutorService sendMessageThreads = Executors.newCachedThreadPool();
        for (Iterator<IChatComponent> messages = this.QUEUED_MESSAGES.iterator(); messages.hasNext(); ) {
            IChatComponent queuedMessage = messages.next();
            messages.remove();
            sendMessageThreads.execute(new SendMessageThread(queuedMessage, this, 2000));
        }
    }

    static void sendThreadSafeMessage(IChatComponent component) {
        ChatComponentText formattedMessage = new ChatComponentText(EnumChatFormatting.GOLD + ""
                + EnumChatFormatting.BOLD + "ARCADELB> ");
        formattedMessage.appendSibling(component).appendSibling(new ChatComponentText(""));
        scheduleMessageOnMainThread(formattedMessage);
    }

    private static void scheduleMessageOnMainThread(IChatComponent message) {
        Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().thePlayer.addChatMessage(message));
    }

    void addQueuedMessage(IChatComponent message) {
        this.QUEUED_MESSAGES.add(message);
    }
}
