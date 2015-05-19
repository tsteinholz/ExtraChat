package com.buuz135.extrachat.commands;

import com.buuz135.extrachat.config.JsonLoader;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;


public class TaggAdd implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (args.<Player>getOne("player").isPresent()) {
            if (args.<String>getOne("tag").isPresent()) {
                JsonLoader.insertTag(args.<String>getOne("tag").get(), args.<Player>getOne("player").get().getName(), args.<Player>getOne("player").get().getUniqueId().toString());
                src.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                        TextColors.GREEN, "Added player " + args.<Player>getOne("player").get().getName() + " to the tag " + args.<String>getOne("tag").get() + "."));
                return CommandResult.success();
            }
        } else {
            src.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                    TextColors.DARK_RED, "That player its not online."));
        }
        return CommandResult.empty();
    }
}
