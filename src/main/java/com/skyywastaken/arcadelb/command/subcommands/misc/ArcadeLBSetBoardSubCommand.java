package com.skyywastaken.arcadelb.command.subcommands.misc;

import com.skyywastaken.arcadelb.command.SubCommand;
import com.skyywastaken.arcadelb.stats.ArcadeLeaderboard;
import com.skyywastaken.arcadelb.stats.game.StatType;
import com.skyywastaken.arcadelb.stats.game.StatTypeHelper;
import com.skyywastaken.arcadelb.util.ConfigManager;
import com.skyywastaken.arcadelb.util.StringUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;

public class ArcadeLBSetBoardSubCommand implements SubCommand {
    private final ArcadeLeaderboard arcadeLeaderboard;
    private final StatTypeHelper statTypeHelper;

    public ArcadeLBSetBoardSubCommand(ArcadeLeaderboard passedBoard, StatTypeHelper statTypeHelper) {
        this.arcadeLeaderboard = passedBoard;
        this.statTypeHelper = statTypeHelper;
    }

    @Override
    public List<String> getCompletions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 0) {
            return new ArrayList<>();
        }
        if (args.length == 1) {
            return StringUtils.getPartialMatches(args[0], getBaseLayerCompletions());
        } else {
            return getCompletionsAtIndex(args);
        }
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (this.arcadeLeaderboard.isBoardSwitching()) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Your leaderboard is currently "
                    + "loading! Wait for it to finish before swapping leaderboards again."));
        } else if (args.length == 0) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "You need to specify a statistic to swap to!"));
        } else if (!this.statTypeHelper.statExists(String.join(".", args))) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "That statistic doesn't exist! " +
                    "Check your spelling and try again."));
        } else {
            StatType newStatType = this.statTypeHelper.getStatTypeFromString(String.join(".", args));
            ConfigManager.setStatTracked(newStatType);
            new Thread(() -> this.arcadeLeaderboard.setLeaderboardFromStatType(newStatType)).start();
        }
    }

    @Override
    public String getCommandName() {
        return "setboard";
    }

    @Override
    public IChatComponent getHelpMessage() {
        return new ChatComponentText(EnumChatFormatting.GREEN
                + "Use this command to change which board is being displayed\n"
                + EnumChatFormatting.RED + "Example: " + EnumChatFormatting.GOLD
                + "/arcadelb setboard partygames wins");
    }

    private List<String> getCompletionsAtIndex(String[] args) {
        if (args.length <= 1) {
            return new ArrayList<>();
        }
        String argsJoined = String.join(".", args);
        ArrayList<String> possibilityList = new ArrayList<>();
        for (String currentString : this.statTypeHelper.getAllStats()) {
            if (currentString.startsWith(argsJoined)) {
                possibilityList.add(currentString);
            }
        }
        ArrayList<String> returnList = new ArrayList<>();
        for (String currentString : possibilityList) {
            int indexToSuggest = args.length - 1;
            String[] possibilitySplit = currentString.split("\\.");
            if (possibilitySplit.length < indexToSuggest) {
                continue;
            }
            String newSuggestion = possibilitySplit[indexToSuggest];
            if (!returnList.contains(newSuggestion)) {
                returnList.add(newSuggestion);
            }
        }
        return returnList;
    }

    private List<String> getBaseLayerCompletions() {
        ArrayList<String> returnList = new ArrayList<>();
        for (String possibleCompletion : this.statTypeHelper.getAllStats()) {
            String currentString = possibleCompletion.split("\\.")[0];
            if (!returnList.contains(currentString)) {
                returnList.add(currentString);
            }
        }
        return returnList;
    }
}