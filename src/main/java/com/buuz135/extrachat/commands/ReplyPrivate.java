package com.buuz135.extrachat.commands;


import com.buuz135.api.Format;
import com.buuz135.extrachat.config.ConfigLoader;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import java.util.HashMap;
import java.util.UUID;

public class ReplyPrivate implements CommandExecutor{
    private Game game;
    public static HashMap<UUID,UUID> lastMessage;

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(!lastMessage.containsKey(game.getServer().getPlayer(src.getName()).get().getUniqueId())){
            src.sendMessage(Texts.of(TextColors.DARK_RED,"You don't have anyone to replay to."));
        }else if (game.getServer().getPlayer(lastMessage.get(game.getServer().getPlayer(src.getName()).get().getUniqueId())).isPresent()){
            Player playerTo = game.getServer().getPlayer(lastMessage.get(game.getServer().getPlayer(src.getName()).get().getUniqueId())).get();
            playerTo.sendMessage(Format.formatPrivateMessage(ConfigLoader.privateMessageFormat,args.<String>getOne("message").get(),
                    src.getName(),playerTo.getName()));
            src.sendMessage(Format.formatPrivateMessage(ConfigLoader.privateMessageFormat,args.<String>getOne("message").get(),
                    src.getName(),playerTo.getName()));
            return CommandResult.success();
        }
        return CommandResult.empty();
    }

    public ReplyPrivate(Game game){
        this.game = game;
        lastMessage = new HashMap<UUID, UUID>();
    }
}
