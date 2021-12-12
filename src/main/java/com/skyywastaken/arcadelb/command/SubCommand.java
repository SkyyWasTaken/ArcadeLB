package com.skyywastaken.arcadelb.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;

public interface SubCommand {
    ArrayList<String> getCompletions(ICommandSender sender, String[] args, BlockPos pos);

    void processCommand(ICommandSender sender, String[] args);

    String getCommandName();

    void sendHelpMessage(ICommandSender sender);
}
