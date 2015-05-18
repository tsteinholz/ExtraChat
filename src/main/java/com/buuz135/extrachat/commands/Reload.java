package com.buuz135.extrachat.commands;


import com.buuz135.extrachat.config.ConfigLoader;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class Reload implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        ConfigLoader.loadConfig();
        src.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                TextColors.GREEN, "Configuration reloaded."));
        return CommandResult.success();
    }
}
