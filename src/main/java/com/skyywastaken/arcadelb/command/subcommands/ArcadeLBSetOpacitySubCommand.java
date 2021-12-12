package com.skyywastaken.arcadelb.command.subcommands;

import com.skyywastaken.arcadelb.util.ConfigManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import com.skyywastaken.arcadelb.command.SubCommand;

import java.util.ArrayList;

public class ArcadeLBSetOpacitySubCommand implements SubCommand {
    @Override
    public ArrayList<String> getCompletions(ICommandSender sender, String[] args, BlockPos pos) {
        return new ArrayList<>();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            sendHelpMessage(sender);
            return;
        }
        int newOpacity;
        try {
            newOpacity = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sendHelpMessage(sender);
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
    public void sendHelpMessage(ICommandSender sender) {
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN
                + "Use this subcommand to change the opacity of the leaderboard. (0-255, default 100)\n"
                + EnumChatFormatting.RED + "Example: " + EnumChatFormatting.GOLD + "/arcadelb setopacity 100"));
    }
}
