package com.skyywastaken.arcadelb.command.subcommands;

import com.skyywastaken.arcadelb.command.SubCommand;
import com.skyywastaken.arcadelb.util.thread.MessageHelper;

public class CommandUtils {
    public static void sendHelpMessage(SubCommand passedSubCommand) {
        MessageHelper.sendThreadSafeMessage(passedSubCommand.getHelpMessage());
    }

    public static int attemptIntegerParseWithHelp(String intToParse, SubCommand sourceCommand) throws NumberFormatException {
        try {
            return Integer.parseInt(intToParse);
        } catch (NumberFormatException e) {
            sendHelpMessage(sourceCommand);
            throw e;
        }
    }

    public static float attemptFloatParseWithHelp(String floatToParse, SubCommand sourceCommand) {
        try {
            return Float.parseFloat(floatToParse);
        } catch (NumberFormatException e) {
            sendHelpMessage(sourceCommand);
            throw e;
        }
    }
}
