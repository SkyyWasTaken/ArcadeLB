package com.skyywastaken.arcadelb.command.subcommands.render;

import com.skyywastaken.arcadelb.command.SubCommand;
import com.skyywastaken.arcadelb.command.subcommands.CommandUtils;
import com.skyywastaken.arcadelb.util.ConfigManager;
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
        Arrays.stream(ElementType.values()).forEach(elementType -> possibleColorChanges.add(elementType.name()));
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
        int redValue;
        int greenValue;
        int blueValue;

        try {
            redValue = CommandUtils.attemptIntegerParseWithHelp(args[1], this);
            greenValue = CommandUtils.attemptIntegerParseWithHelp(args[2], this);
            blueValue = CommandUtils.attemptIntegerParseWithHelp(args[3], this);
        } catch (NumberFormatException e) {
            return;
        }
        if (redValue < 0 || greenValue < 0 || blueValue < 0) {
            MessageHelper.sendThreadSafeMessage(
                    new ChatComponentText(EnumChatFormatting.RED + "You can't set a color to a value below 0!"));
        } else if (redValue > 255 || greenValue > 255 || blueValue > 255) {
            MessageHelper.sendThreadSafeMessage(new ChatComponentText(EnumChatFormatting.RED +
                    "You can't set a color to a value over 255!"));
        }
        int newColorValue = (0xFF << 24) + (redValue << 16) + (greenValue << 8) + blueValue;
        switch (elementType) {
            case YOUR_NAME:
                ConfigManager.setYourNameColor(newColorValue);
                break;
            case OTHERS_NAMES:
                ConfigManager.setOthersNameColor(newColorValue);
                break;
            case HEADER:
                ConfigManager.setHeaderColor(newColorValue);
                break;
            case PLACE:
                ConfigManager.setPlaceColor(newColorValue);
                break;
            case SCORE:
                ConfigManager.setScoreColor(newColorValue);
                break;
            case MISC:
                ConfigManager.setMiscColor(newColorValue);
        }
        MessageHelper.sendThreadSafeMessage(new ChatComponentText(EnumChatFormatting.GREEN + "" + elementType + " has been set to " + EnumChatFormatting.DARK_RED + redValue + " " + EnumChatFormatting.DARK_GREEN + greenValue + " " + EnumChatFormatting.DARK_BLUE + blueValue));
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
        YOUR_NAME, OTHERS_NAMES, PLACE, SCORE, HEADER, MISC, STATUS;

        private ElementType getFromString(String passedString) {
            try {
                return valueOf(passedString.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }
}
