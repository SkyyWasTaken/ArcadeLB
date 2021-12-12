package com.skyywastaken.arcadelb.command.subcommands;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import com.skyywastaken.arcadelb.command.SubCommand;
import com.skyywastaken.arcadelb.util.ConfigManager;

import java.util.ArrayList;

public class ArcadeLBSetEnabledSubCommand implements SubCommand {
    @Override
    public ArrayList<String> getCompletions(ICommandSender sender, String[] args, BlockPos pos) {
        return new ArrayList<>();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            sendHelpMessage(sender);
        }
        boolean typedValue = Boolean.parseBoolean(args[0]);
        ConfigManager.setLeaderboardEnabled(typedValue);
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "The leaderboard has been "
                + (typedValue ? "enabled" : "disabled") + "!"));
    }

    @Override
    public String getCommandName() {
        return "setenabled";
    }

    @Override
    public void sendHelpMessage(ICommandSender sender) {
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN
                + "Use this subcommand to enable or disable the leaderboard. (true/false, default: true)\n"
                + EnumChatFormatting.RED + "Example: " + EnumChatFormatting.GOLD + "/arcadelb setenabled true"));
    }
}
