package com.skyywastaken.arcadelb.command.subcommands;

import com.skyywastaken.arcadelb.command.SubCommand;
import com.skyywastaken.arcadelb.util.thread.MessageHelper;

public class CommandUtils {
    public static void sendHelpMessage(SubCommand passedSubCommand) {
        MessageHelper.sendThreadSafeMessage(passedSubCommand.getHelpMessage());
    }

    public static int AttemptIntegerParseWithHelp(String intToParse, SubCommand sourceCommand) throws NumberFormatException {
        try {
            return Integer.parseInt(intToParse);
        } catch (NumberFormatException e) {
            sendHelpMessage(sourceCommand);
            throw e;
        }
    }

    public static float attemptFloatParseWithHelp(String floatToParse, SubCommand sourceCommand) {
        try {
            float parsedFloat = Float.parseFloat(floatToParse);
            if (parsedFloat < 0 || parsedFloat > 1) {
                throw new NumberFormatException();
            }
            return parsedFloat;
        } catch (NumberFormatException e) {
            sendHelpMessage(sourceCommand);
            throw e;
        }
    }
}
