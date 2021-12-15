package com.skyywastaken.arcadelb.command.subcommands.render;

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

public class ArcadeLBSetDisplayPlayerSubCommand implements SubCommand {
    @Override
    public List<String> getCompletions(ICommandSender sender, String[] args, BlockPos pos) {
        return new ArrayList<>();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            CommandUtils.sendHelpMessage(this);
        }
        boolean typedValue = Boolean.parseBoolean(args[0]);
        ConfigManager.setDisplayPlayer(typedValue);
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Your score is now "
                + (typedValue ? "shown" : "hidden") + "!"));
    }

    @Override
    public String getCommandName() {
        return "setdisplayplayer";
    }

    @Override
    public IChatComponent getHelpMessage() {
        return new ChatComponentText(EnumChatFormatting.GREEN
                + "Use this subcommand to show/hide your score. (true/false, default: true)\n"
                + EnumChatFormatting.RED + "Example: " + EnumChatFormatting.GOLD + "/arcadelb setdisplayplayer true");
    }
}
