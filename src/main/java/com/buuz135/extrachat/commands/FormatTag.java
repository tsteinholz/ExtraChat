package com.buuz135.extrachat.commands;


import com.buuz135.extrachat.config.ConfigLoader;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class FormatTag implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!args.<String>getOne("format").get().contains("%TAG%")) {
            src.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                    TextColors.DARK_RED, "Please, introduce a valid format."));
            return CommandResult.empty();
        }
        ConfigLoader.saveConfig(args.<String>getOne("format").get(), "formatTag");
        src.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                TextColors.GREEN, "Format saved."));
        return CommandResult.success();
    }
}
