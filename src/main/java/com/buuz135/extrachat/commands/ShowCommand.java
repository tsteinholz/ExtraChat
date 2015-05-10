package com.buuz135.extrachat.commands;


import com.buuz135.extrachat.ExtraChat;
import com.google.common.base.Optional;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.action.HoverAction;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

import java.util.List;

public class ShowCommand implements CommandCallable {
    @Override
    public Optional<CommandResult> process(CommandSource source, String arguments) throws CommandException {
        if (ExtraChat.game.getServer().getPlayer(source.getName()).get().getItemInHand().isPresent()) {
            ExtraChat.game.getServer().broadcastMessage(Texts.of(source.getName() + " shows: ").builder()
                    .append(Texts.of("[", Texts.of(ExtraChat.game.getServer().getPlayer(source.getName()).get().getItemInHand().get())
                            , TextColors.WHITE, "]").builder().onHover(new HoverAction.ShowItem(ExtraChat.game.getServer()
                            .getPlayer(source.getName()).get().getItemInHand().get())).build()).build());
        } else {
            source.sendMessage(Texts.of(TextColors.DARK_RED, "You don't have an item in your hand."));
        }
        return Optional.of(CommandResult.success());
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        return null;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return true;
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return null;
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return null;
    }

    @Override
    public Text getUsage(CommandSource source) {
        return null;
    }
}
