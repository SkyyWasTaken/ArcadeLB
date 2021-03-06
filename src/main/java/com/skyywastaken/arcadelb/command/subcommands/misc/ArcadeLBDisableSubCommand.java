package com.skyywastaken.arcadelb.command.subcommands.misc;

import com.skyywastaken.arcadelb.command.SubCommand;
import com.skyywastaken.arcadelb.util.ConfigManager;
import com.skyywastaken.arcadelb.util.thread.MessageHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;

public class ArcadeLBDisableSubCommand implements SubCommand {
    @Override
    public List<String> getCompletions(ICommandSender sender, String[] args, BlockPos pos) {
        return new ArrayList<>();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!ConfigManager.getLeaderboardEnabled()) {
            MessageHelper.sendNullAndThreadSafeMessage(new ChatComponentText(EnumChatFormatting.RED
                    + "The leaderboard is already disabled!"));
            return;
        }
        ConfigManager.setLeaderboardEnabled(false);
        MessageHelper.sendNullAndThreadSafeMessage(new ChatComponentText(EnumChatFormatting.GREEN
                + "The leaderboard has been disabled!"));
    }

    @Override
    public String getCommandName() {
        return "disable";
    }

    @Override
    public IChatComponent getHelpMessage() {
        return new ChatComponentText(EnumChatFormatting.GREEN
                + "Use this subcommand to disable the leaderboard.\n"
                + EnumChatFormatting.RED + "Example: " + EnumChatFormatting.GOLD + "/arcadelb disable");
    }
}
