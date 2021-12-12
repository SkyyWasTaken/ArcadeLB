package com.skyywastaken.arcadelb.command.subcommands.render;

import com.skyywastaken.arcadelb.command.subcommands.CommandUtils;
import com.skyywastaken.arcadelb.util.ConfigManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import com.skyywastaken.arcadelb.command.SubCommand;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;

public class ArcadeLBSetOpacitySubCommand implements SubCommand {
    @Override
    public ArrayList<String> getCompletions(ICommandSender sender, String[] args, BlockPos pos) {
        return new ArrayList<>();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            CommandUtils.sendHelpMessage(this);
            return;
        }
        int newOpacity;
        try {
            newOpacity = CommandUtils.AttemptIntegerParseWithHelp(args[0], this);
        } catch (NumberFormatException e) {
            return;
        }
        ConfigManager.setOpacity(newOpacity);
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Opacity successfully set to " + newOpacity + "!"));
    }

    @Override
    public String getCommandName() {
        return "setopacity";
    }

    @Override
    public IChatComponent getHelpMessage() {
        return new ChatComponentText(EnumChatFormatting.GREEN
                + "Use this subcommand to change the opacity of the leaderboard. (0-255, default 100)\n"
                + EnumChatFormatting.RED + "Example: " + EnumChatFormatting.GOLD + "/arcadelb setopacity 100");
    }
}
