package com.skyywastaken.arcadelb.command.subcommands.update;

import com.skyywastaken.arcadelb.command.SubCommand;
import com.skyywastaken.arcadelb.command.subcommands.CommandUtils;
import com.skyywastaken.arcadelb.util.ConfigManager;
import com.skyywastaken.arcadelb.util.score.HypixelQueryHelper;
import com.skyywastaken.arcadelb.util.thread.MessageHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ArcadeLBSetAPIKeySubCommand implements SubCommand {
    @Override
    public List<String> getCompletions(ICommandSender sender, String[] args, BlockPos pos) {
        return new ArrayList<>();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            CommandUtils.sendHelpMessage(this);
            return;
        }
        MessageHelper.sendThreadSafeMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Checking your API key..."));
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(checkValidityAndSetAPIKey(args[0]));
    }

    @Override
    public String getCommandName() {
        return "setapikey";
    }

    @Override
    public IChatComponent getHelpMessage() {
        return new ChatComponentText(EnumChatFormatting.GREEN
                + "Use this subcommand to give the mod your Hypixel API key.\n"
                + EnumChatFormatting.RED + "Example: " + EnumChatFormatting.GOLD + "/arcadelb setapikey (key)");
    }


    private Runnable checkValidityAndSetAPIKey(String apiKey) {
        return () -> {
            boolean keyIsValid;
            keyIsValid = HypixelQueryHelper.runKeyCheckWithFeedback(apiKey);
            if (keyIsValid) {
                ConfigManager.setAPIKey(apiKey);
                MessageHelper.sendThreadSafeMessage(new ChatComponentText(EnumChatFormatting.GREEN +
                        "API key set successfully!"));
            }
        };
    }
}
