package com.buuz135.extrachat.broadcast;


import com.buuz135.extrachat.ExtraChat;
import com.buuz135.extrachat.config.ConfigLoader;
import com.buuz135.extrachat.perms.PermsUtils;
import com.google.common.base.Optional;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

import java.util.ArrayList;
import java.util.List;

public class BRCommand implements CommandCallable {


    @Override
    public Optional<CommandResult> process(CommandSource source, String arguments) throws CommandException {
        if (!source.hasPermission(PermsUtils.BR)) return Optional.of(CommandResult.empty());
        ExtraChat.game.getServer().broadcastMessage(ConfigLoader.broadcastTag.builder().append(Texts.fromLegacy(arguments, '&')).build());
        return Optional.of(CommandResult.success());
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        return new ArrayList<String>();
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return true;
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        Object o = "Broadcast a message";
        return Optional.of(Texts.of(o));
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        Object o = "Broadcast a message";
        return Optional.of(Texts.of(o));
    }

    @Override
    public Text getUsage(CommandSource source) {
        Object o = "Broadcast a message";
        return Texts.of(o);
    }
}
