package com.buuz135.extrachat.commands.channels;


import com.buuz135.api.ChatChannel;
import com.buuz135.extrachat.ExtraChat;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class Talk implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        ChatChannel.setWrittingChannel(ExtraChat.game.getServer().getPlayer(src.getName()).get(), args.<String>getOne("channel").get());
        return CommandResult.success();
    }
}
