package com.skyywastaken.arcadelb.command.subcommands.render;

import com.skyywastaken.arcadelb.command.SubCommand;
import com.skyywastaken.arcadelb.command.subcommands.CommandUtils;
import com.skyywastaken.arcadelb.util.StringUtils;
import com.skyywastaken.arcadelb.util.thread.MessageHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ArcadeLBSetColorSubCommand implements SubCommand {
    private final List<String> possibleColorChanges = new ArrayList<>();

    public ArcadeLBSetColorSubCommand() {
        ArrayList<String> elementTypes = new ArrayList<>();
        Arrays.stream(ElementType.values()).forEach(elementType -> elementTypes.add(elementType.name()));
    }

    @Override
    public List<String> getCompletions(ICommandSender sender, String[] args, BlockPos pos) {
        if(args.length == 1) {
            return StringUtils.getPartialMatches(args[0], possibleColorChanges);
        }
        return new ArrayList<>();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 4) {
            CommandUtils.sendHelpMessage(this);
            return;
        }
        ElementType elementType = ElementType.HEADER.getFromString(args[0]);
        if (elementType == null) {
            MessageHelper.sendThreadSafeMessage(new ChatComponentText(EnumChatFormatting.RED + "'" + args[0]
                    + "' is not a valid element type!"));
            return;
        }

    }

    @Override
    public String getCommandName() {
        return "setcolor";
    }

    @Override
    public IChatComponent getHelpMessage() {
        return new ChatComponentText(EnumChatFormatting.GREEN
                + "Use this subcommand to change the color of various leaderboard elements "
                + "(Max color value: 255, 255, 255. Min: 0, 0, 0)\n"
                + EnumChatFormatting.RED + "Example: " + EnumChatFormatting.GOLD + "/arcadelb setcolor <element> "
                + "<R> <G> <B>");
    }

    private enum ElementType {
        YOUR_NAME, OTHERS_NAMES, PLACE, SCORE, HEADER;

        private List<String> getValueStrings() {
            ArrayList<String> returnList = new ArrayList<>();
            Arrays.stream(ElementType.values()).forEach(elementType -> returnList.add(elementType.name()));
            return returnList;
        }

        private ElementType getFromString(String passedString) {
            try {
                return valueOf(passedString.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }
}
