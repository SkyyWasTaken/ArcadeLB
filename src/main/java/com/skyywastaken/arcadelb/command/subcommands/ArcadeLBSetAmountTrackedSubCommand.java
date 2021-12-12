package com.skyywastaken.arcadelb.command.subcommands;

import com.skyywastaken.arcadelb.command.SubCommand;
import com.skyywastaken.arcadelb.util.ConfigManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;

public class ArcadeLBSetAmountTrackedSubCommand implements SubCommand {
    @Override
    public ArrayList<String> getCompletions(ICommandSender sender, String[] args, BlockPos pos) {
        return new ArrayList<>();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            sendHelpMessage(sender);
            return;
        }
        int newupdateAmount;
        try {
            newupdateAmount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sendHelpMessage(sender);
            return;
        }
        ConfigManager.setTotalTracked(newupdateAmount);
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN
                + "Total tracked player count has been set to " + newupdateAmount + "!"));
    }

    @Override
    public String getCommandName() {
        return "setamounttracked";
    }

    @Override
    public void sendHelpMessage(ICommandSender sender) {
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN
                + "Use this subcommand to change how many players to keep track of (range 0-100, default 50)\n"
                + EnumChatFormatting.RED + "Example: " + EnumChatFormatting.GOLD + "/arcadelb setamounttracked 50"));
    }
}
