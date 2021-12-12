package com.skyywastaken.arcadelb.command.subcommands.render;

import com.skyywastaken.arcadelb.command.subcommands.CommandUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import com.skyywastaken.arcadelb.command.SubCommand;
import com.skyywastaken.arcadelb.util.ConfigManager;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;

public class ArcadeLBSetYOffsetSubCommand implements SubCommand {
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
        double typedValue;
        try {
            typedValue = CommandUtils.attemptFloatParseWithHelp(args[0], this);
        } catch (NumberFormatException e) {
            return;
        }
        ConfigManager.setYOffset((float) typedValue);
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN
                + "Y offset successfully set to " + typedValue));
    }

    @Override
    public String getCommandName() {
        return "setyoffset";
    }

    @Override
    public IChatComponent getHelpMessage() {
        return new ChatComponentText(EnumChatFormatting.GREEN
                + "Use this subcommand to change the y offset of the leaderboard (0.0-1.0, default 0.5)\n"
                + EnumChatFormatting.RED + "Example: " + EnumChatFormatting.GOLD + "/arcadelb setyoffset 0");
    }
}
