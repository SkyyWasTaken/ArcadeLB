package com.skyywastaken.arcadelb.command.subcommands.update;

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

public class ArcadeLBSetAmountTrackedSubCommand implements SubCommand {
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
        int newUpdateAmount;
        try {
            newUpdateAmount = CommandUtils.attemptIntegerParseWithHelp(args[0], this);
        } catch (NumberFormatException e) {
            return;
        }
        ConfigManager.setTotalTracked(newUpdateAmount);
        MessageHelper.sendThreadSafeMessage(new ChatComponentText(EnumChatFormatting.GREEN
                + "Total tracked player count has been set to " + newUpdateAmount + "!"));
    }

    @Override
    public String getCommandName() {
        return "setamounttracked";
    }

    @Override
    public IChatComponent getHelpMessage() {
        return new ChatComponentText(EnumChatFormatting.GREEN
                + "Use this subcommand to change how many players to keep track of (range 0-100, default 50)\n"
                + EnumChatFormatting.RED + "Example: " + EnumChatFormatting.GOLD + "/arcadelb setamounttracked 50");
    }
}
