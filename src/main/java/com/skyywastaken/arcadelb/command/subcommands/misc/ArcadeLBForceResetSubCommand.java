package com.skyywastaken.arcadelb.command.subcommands.misc;

import com.skyywastaken.arcadelb.command.SubCommand;
import com.skyywastaken.arcadelb.stats.ArcadeLeaderboard;
import com.skyywastaken.arcadelb.util.thread.MessageHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;

public class ArcadeLBForceResetSubCommand implements SubCommand {
    private final ArcadeLeaderboard PASSED_LEADERBOARD;

    public ArcadeLBForceResetSubCommand(ArcadeLeaderboard passedBoard) {
        this.PASSED_LEADERBOARD = passedBoard;
    }

    @Override
    public List<String> getCompletions(ICommandSender sender, String[] args, BlockPos pos) {
        return new ArrayList<>();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        this.PASSED_LEADERBOARD.reset();
        MessageHelper.sendNullAndThreadSafeMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Reset complete!"));
    }

    @Override
    public String getCommandName() {
        return "forcereset";
    }

    @Override
    public IChatComponent getHelpMessage() {
        return new ChatComponentText(EnumChatFormatting.GREEN
                + "Use this subcommand to force-reset the leaderboard in case something has gone wrong.\n"
                + EnumChatFormatting.RED + "Example: " + EnumChatFormatting.GOLD + "/arcadelb forcereset");
    }
}
