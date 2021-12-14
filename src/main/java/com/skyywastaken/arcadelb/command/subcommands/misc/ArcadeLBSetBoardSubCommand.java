package com.skyywastaken.arcadelb.command.subcommands.misc;

import com.skyywastaken.arcadelb.command.SubCommand;
import com.skyywastaken.arcadelb.stats.ArcadeLeaderboard;
import com.skyywastaken.arcadelb.stats.game.StatType;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.HashMap;

public class ArcadeLBSetBoardSubCommand implements SubCommand {
    private final HashMap<String, StatType> statTypeHashMap = new HashMap<>();
    private final ArcadeLeaderboard arcadeLeaderboard;

    public ArcadeLBSetBoardSubCommand(ArcadeLeaderboard passedBoard) {
        this.arcadeLeaderboard = passedBoard;
    }

    @Override
    public ArrayList<String> getCompletions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (this.arcadeLeaderboard.isBoardSwitching()) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Your leaderboard is currently "
                    + "loading! Wait for it to finish before swapping leaderboards again."));
        }
    }

    @Override
    public String getCommandName() {
        return null;
    }

    @Override
    public IChatComponent getHelpMessage() {
        return new ChatComponentText(EnumChatFormatting.GREEN
                + "Use this command to change which board is being displayed\n"
                + EnumChatFormatting.RED + "Example: " + EnumChatFormatting.GOLD
                + "/arcadelb setboard partygames wins");
    }
}