package com.skyywastaken.arcadelb.command.subcommands.update;

import com.skyywastaken.arcadelb.command.SubCommand;
import com.skyywastaken.arcadelb.command.subcommands.CommandUtils;
import com.skyywastaken.arcadelb.util.ConfigManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;

public class ArcadeLBSetUpdateAmountSubCommand implements SubCommand {
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
            newUpdateAmount = CommandUtils.AttemptIntegerParseWithHelp(args[0], this);
        } catch (NumberFormatException e) {
            return;
        }
        ConfigManager.setUpdateAmount(newUpdateAmount);
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Concurrent update amount set to " + newUpdateAmount + "!"));
    }

    @Override
    public String getCommandName() {
        return "setconcurrentupdateamount";
    }

    @Override
    public IChatComponent getHelpMessage() {
        return new ChatComponentText(EnumChatFormatting.GREEN
                + "Use this subcommand to change how many players update at once (range 0-100, default 5)\n"
                + EnumChatFormatting.RED + "Example: " + EnumChatFormatting.GOLD + "/arcadelb setconcurrentupdateamount 5");
    }
}
