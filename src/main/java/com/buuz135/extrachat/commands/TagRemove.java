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


public class TagRemove implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        JsonLoader.removeTag(args.<Player>getOne("player").get().getUniqueId().toString());
        src.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                "Removed tag from the player " + args.<Player>getOne("player").get().getName() + ".").builder().color(TextColors.GREEN)
                .build());
        return CommandResult.success();
    }
}
