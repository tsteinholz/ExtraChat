package com.buuz135.extrachat.broadcast;


import com.buuz135.extrachat.ExtraChat;
import com.buuz135.extrachat.config.ConfigLoader;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class BRCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        ExtraChat.game.getServer().getBroadcastSink().sendMessage(ConfigLoader.broadcastTag.builder().append(Texts.fromLegacy(args.<String>getOne("broadcast").get(), '&')).build());
        return CommandResult.success();
    }
}
