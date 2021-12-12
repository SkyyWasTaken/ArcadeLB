package com.skyywastaken.arcadelb.command;

import com.skyywastaken.arcadelb.command.subcommands.ArcadeLBSetAPIKeySubCommand;
import com.skyywastaken.arcadelb.command.subcommands.ArcadeLBSetUpdateAmountSubCommand;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import com.skyywastaken.arcadelb.command.subcommands.ArcadeLBSetDisplayPlayerSubCommand;
import com.skyywastaken.arcadelb.command.subcommands.ArcadeLBSetEnabledSubCommand;
import com.skyywastaken.arcadelb.command.subcommands.ArcadeLBSetOpacitySubCommand;
import com.skyywastaken.arcadelb.command.subcommands.ArcadeLBSetXOffsetSubCommand;
import com.skyywastaken.arcadelb.command.subcommands.ArcadeLBSetYOffsetSubCommand;
import com.skyywastaken.arcadelb.command.subcommands.ArcadeLbRefreshSubCommand;
import com.skyywastaken.arcadelb.stats.ArcadeLeaderboard;
import com.skyywastaken.arcadelb.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ArcadeLBCommand implements ICommand {
    private final ArcadeLeaderboard ARCADE_LEADERBOARD;
    private final HashMap<String, SubCommand> subCommands = new HashMap<>();

    public ArcadeLBCommand(ArcadeLeaderboard passedLeaderboard) {
        this.ARCADE_LEADERBOARD = passedLeaderboard;
        registerSubCommands();
    }

    @Override
    public String getCommandName() {
        return "arcadelb";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "I hate this error message.";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<>();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            sendInvalidCommandFeedback(sender, null);
        } else if (subCommands.containsKey(args[0])) {
            String[] argsWithoutSubCommandName = Arrays.copyOfRange(args, 1, args.length);
            subCommands.get(args[0]).processCommand(sender, argsWithoutSubCommandName);
        } else {
            sendInvalidCommandFeedback(sender, args[0]);
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 0) {
            return new ArrayList<>(this.subCommands.keySet());
        } else if (args.length == 1) {
            return StringUtils.getPartialMatches(args[0], this.subCommands.keySet());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }

    private void registerSubCommands() {
        registerSubCommand(new ArcadeLBSetOpacitySubCommand());
        registerSubCommand(new ArcadeLBSetXOffsetSubCommand());
        registerSubCommand(new ArcadeLBSetYOffsetSubCommand());
        registerSubCommand(new ArcadeLBSetEnabledSubCommand());
        registerSubCommand(new ArcadeLbRefreshSubCommand(this.ARCADE_LEADERBOARD));
        registerSubCommand(new ArcadeLBSetDisplayPlayerSubCommand());
        registerSubCommand(new ArcadeLBSetUpdateAmountSubCommand());
        registerSubCommand(new ArcadeLBSetAPIKeySubCommand());
    }

    private void registerSubCommand(SubCommand commandToRegister) {
        this.subCommands.put(commandToRegister.getCommandName(), commandToRegister);
    }

    private void sendInvalidCommandFeedback(ICommandSender commandSender, String attemptedCommand) {
        String failureMessage;
        if (attemptedCommand == null || attemptedCommand.equals("")) {
            failureMessage = EnumChatFormatting.RED + "You need to type a subcommand!";
        } else {
            failureMessage = EnumChatFormatting.LIGHT_PURPLE + attemptedCommand + EnumChatFormatting.RED
                    + " is not a valid subcommand!";
        }

        StringBuilder subCommandList = new StringBuilder();
        subCommandList.append(EnumChatFormatting.GOLD).append("Valid commands: \n").append(EnumChatFormatting.RESET);
        int i = 0;
        for (String currentSubCommandName : this.subCommands.keySet()) {
            subCommandList.append(EnumChatFormatting.GREEN);
            subCommandList.append(currentSubCommandName);
            if (i++ < this.subCommands.keySet().size() - 1) {
                subCommandList.append(EnumChatFormatting.GOLD).append(", ");
            }
            subCommandList.append(EnumChatFormatting.RESET);
        }
        commandSender.addChatMessage(new ChatComponentText(failureMessage));
        commandSender.addChatMessage(new ChatComponentText(subCommandList.toString()));
    }
}
