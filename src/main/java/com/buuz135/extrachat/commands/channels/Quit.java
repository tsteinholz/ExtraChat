package com.buuz135.extrachat.commands.channels;


import com.buuz135.api.ChatChannel;
import com.buuz135.extrachat.ExtraChat;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class Quit implements CommandExecutor{
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        ChatChannel.removePlayerListening(ExtraChat.game.getServer().getPlayer(src.getName()).get(),args.<String>getOne("channel").get(),ExtraChat.game);
        return CommandResult.success();
    }
}
