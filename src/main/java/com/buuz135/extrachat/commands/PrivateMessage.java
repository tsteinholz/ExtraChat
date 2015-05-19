package com.buuz135.extrachat.commands;


import com.buuz135.api.Format;
import com.buuz135.extrachat.config.ConfigLoader;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class PrivateMessage implements CommandExecutor {
    private Game game;

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        args.<Player>getOne("playerDest").get().sendMessage(Format.formatPrivateMessage(ConfigLoader.privateMessageFormat
                , args.<String>getOne("message").get(), src.getName(), args.<Player>getOne("playerDest").get().getName()));
        src.sendMessage(Format.formatPrivateMessage(ConfigLoader.privateMessageFormat
                , args.<String>getOne("message").get(), src.getName(), args.<Player>getOne("playerDest").get().getName()));
        ReplyPrivate.lastMessage.put(args.<Player>getOne("playerDest").get().getUniqueId(), game.getServer().getPlayer(src.getName()).get().getUniqueId());
        return CommandResult.success();
    }

    public PrivateMessage(Game game) {
        this.game = game;
    }
}
