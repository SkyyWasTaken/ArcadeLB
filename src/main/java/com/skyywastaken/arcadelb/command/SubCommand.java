package com.skyywastaken.arcadelb.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;

import java.util.List;

public interface SubCommand {
    List<String> getCompletions(ICommandSender sender, String[] args, BlockPos pos);

    void processCommand(ICommandSender sender, String[] args);

    String getCommandName();

    IChatComponent getHelpMessage();
}
