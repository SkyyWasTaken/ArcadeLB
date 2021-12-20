package com.skyywastaken.arcadelb.command.subcommands.render;

import com.skyywastaken.arcadelb.command.SubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;

public class ArcadeLBSetColorSubCommand implements SubCommand {
    private final List<String> possibleColorChanges = new ArrayList<>();

    public ArcadeLBSetColorSubCommand() {
        possibleColorChanges.add("yourname");
        possibleColorChanges.add("othersnames");
        possibleColorChanges.add("place");
        possibleColorChanges.add("score");
        possibleColorChanges.add("header");
    }

    @Override
    public List<String> getCompletions(ICommandSender sender, String[] args, BlockPos pos) {
        return new ArrayList<>();
        /*
        if(args.length == 1) {
            return StringUtils.getPartialMatches(args[0], possibleColorChanges)
        }
        */
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

    }

    @Override
    public String getCommandName() {
        return "setcolor";
    }

    @Override
    public IChatComponent getHelpMessage() {
        return new ChatComponentText(EnumChatFormatting.GREEN
                + "Use this subcommand to change the color of various leaderboard elements\n"
                + EnumChatFormatting.RED + "Example: " + EnumChatFormatting.GOLD + "/arcadelb setcolor <element> "
                + "<color>");
    }
}
