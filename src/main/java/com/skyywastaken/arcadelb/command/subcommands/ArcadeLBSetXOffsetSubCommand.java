package com.skyywastaken.arcadelb.command.subcommands;

import com.skyywastaken.arcadelb.util.ConfigManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import com.skyywastaken.arcadelb.command.SubCommand;

import java.util.ArrayList;

public class ArcadeLBSetXOffsetSubCommand implements SubCommand {
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
        double typedValue;
        try {
            typedValue = Double.parseDouble(args[0]);
        } catch (NumberFormatException numberFormatException) {
            sendHelpMessage(sender);
            return;
        }
        if (typedValue < 0 || typedValue > 1) {
            sendHelpMessage(sender);
            return;
        }
        ConfigManager.setXOffset((float) typedValue);
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN
                + "X offset successfully set to " + typedValue));
    }

    @Override
    public String getCommandName() {
        return "setxoffset";
    }

    @Override
    public void sendHelpMessage(ICommandSender sender) {
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN
                + "Use this subcommand to change the x offset of the leaderboard (0.0-1.0, default 0.0)\n"
                + EnumChatFormatting.RED + "Example: " + EnumChatFormatting.GOLD + "/arcadelb setxoffset 0"));
    }
}
