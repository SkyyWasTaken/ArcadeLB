package com.skyywastaken.arcadelb.command.subcommands.update;

import com.skyywastaken.arcadelb.stats.ArcadeLeaderboard;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import com.skyywastaken.arcadelb.command.SubCommand;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;

public class ArcadeLbRefreshSubCommand implements SubCommand {
    private final ArcadeLeaderboard ARCADE_LEADERBOARD;

    public ArcadeLbRefreshSubCommand(ArcadeLeaderboard passedLeaderboard) {
        this.ARCADE_LEADERBOARD = passedLeaderboard;
    }

    @Override
    public ArrayList<String> getCompletions(ICommandSender sender, String[] args, BlockPos pos) {
        return new ArrayList<>();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        Thread thread = new Thread(ARCADE_LEADERBOARD::updateLeaderboard);
        thread.start();
    }

    @Override
    public String getCommandName() {
        return "refresh";
    }

    @Override
    public IChatComponent getHelpMessage() {
        return new ChatComponentText(EnumChatFormatting.GREEN
                + "This command forces the mod to sync the leaderboard with Hypixel's API. Use sparingly!\n"
                + EnumChatFormatting.RED + "Example: " + EnumChatFormatting.GOLD + "/arcadeapi refresh");
    }
}
