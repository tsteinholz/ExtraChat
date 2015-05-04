package com.buuz135.extrachat.broadcast;


import com.buuz135.extrachat.ExtraChat;
import com.buuz135.extrachat.perms.PermsUtils;
import com.google.common.base.Optional;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

import java.util.List;

public class BRCommand implements CommandCallable {


    @Override
    public Optional<CommandResult> process(CommandSource source, String arguments) throws CommandException {
        if (!source.hasPermission(PermsUtils.BR)) return Optional.of(CommandResult.empty());
        ExtraChat.game.getServer().broadcastMessage(Texts.fromLegacy(arguments, '&'));
        return Optional.of(CommandResult.success());
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        return null;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return false;
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
