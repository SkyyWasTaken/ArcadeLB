package com.skyywastaken.arcadelb.command.subcommands.render;

import com.skyywastaken.arcadelb.command.SubCommand;
import com.skyywastaken.arcadelb.command.subcommands.CommandUtils;
import com.skyywastaken.arcadelb.util.ConfigManager;
import com.skyywastaken.arcadelb.util.thread.MessageHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;

public class ArcadeLBSetScaleSubCommand implements SubCommand {
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
        float typedValue;
        try {
            typedValue = CommandUtils.attemptFloatParseWithHelp(args[0], this);
        } catch (NumberFormatException e) {
            return;
        }
        if (typedValue <= 0 || typedValue > 50) {
            CommandUtils.sendHelpMessage(this);
            return;
        }
        ConfigManager.setLeaderboardScale(typedValue);
        MessageHelper.sendNullAndThreadSafeMessage(new ChatComponentText(EnumChatFormatting.GREEN
                + "Leaderboard scale successfully set to " + typedValue));
    }

    @Override
    public String getCommandName() {
        return "setscale";
    }

    @Override
    public IChatComponent getHelpMessage() {
        return new ChatComponentText(EnumChatFormatting.GREEN
                + "Use this subcommand to change the x offset of the leaderboard (0.0001-50.0, default 1.0)\n"
                + EnumChatFormatting.RED + "Example: " + EnumChatFormatting.GOLD + "/arcadelb setscale 1");
    }
}
