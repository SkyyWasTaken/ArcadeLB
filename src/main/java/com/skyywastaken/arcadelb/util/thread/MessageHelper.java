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
        ThreadListeners threadListeners = new ThreadListeners(this);
        MinecraftForge.EVENT_BUS.register(threadListeners);
    }

    public static void sendThreadSafeMessage(IChatComponent chatComponent) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            ChatComponentText modNameMessage = new ChatComponentText(EnumChatFormatting.GOLD + ""
                    + EnumChatFormatting.BOLD + "ARCADELB>" + EnumChatFormatting.RESET);
            modNameMessage.appendSibling(chatComponent);
            MessageHelper.sendThreadSafeMessage(modNameMessage);
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

    void addQueuedMessage(IChatComponent message) {
        this.QUEUED_MESSAGES.add(message);
    }
}
