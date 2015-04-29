package com.buuz135.extrachat.main.commands;

import com.buuz135.extrachat.main.ExtraChat;
import com.buuz135.extrachat.main.config.ConfigLoader;
import com.buuz135.extrachat.main.config.JsonLoader;
import com.google.common.base.Optional;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

import java.util.ArrayList;
import java.util.List;


public class ExtraChatCommand implements CommandCallable {
    private String usage = "USAGE: /ec <config|tagadd> <[reload|format]|tag> <player>";
    private Object description = "Main command for ExtraChat";

    @Override
    public Optional<CommandResult> process(CommandSource source, String arguments) throws CommandException {
        if (testPermission(source)) {
            String[] args = arguments.split(" ");
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("config")) {
                    if (args.length > 1) {
                        if (args[1].equalsIgnoreCase("reload") && args.length == 2) {
                            ConfigLoader.loadConfig();
                            source.sendMessage(Texts.of("Configuration reloaded.").builder()
                                    .color(TextColors.GREEN).build());
                        } else if (args[1].equalsIgnoreCase("format")) {
                            if (args.length == 2) {
                                source.sendMessage(Texts.of("Please, introduce a valid format.").builder()
                                        .color(TextColors.DARK_RED).build());
                            } else {
                                ConfigLoader.saveConfig(arguments.substring(arguments.indexOf("format") + 7));
                                source.sendMessage(Texts.of("Format saved.").builder().color(TextColors.GREEN)
                                        .build());
                            }
                        }
                    } else {
                        source.sendMessage(Texts.of(usage).builder().color(TextColors.RED).build());
                    }
                } else if (args[0].equalsIgnoreCase("tagadd")) {
                    if (args.length == 3){
                        if (ExtraChat.game.getServer().getPlayer(args[2]).isPresent()){
                            JsonLoader.insertTag(args[1],args[2],ExtraChat.game.getServer().getPlayer(args[2]).get().getUniqueId().toString());
                        }else{
                            source.sendMessage(Texts.of("That player its not online").builder().color(TextColors.RED).build());
                        }
                    }else{
                        source.sendMessage(Texts.of(usage).builder().color(TextColors.RED).build());
                    }
                } else {
                    source.sendMessage(Texts.of(usage).builder().color(TextColors.RED).build());
                }
            } else {
                source.sendMessage(Texts.of(usage).builder().color(TextColors.RED).build());
            }
        } else {
            source.sendMessage(Texts.of("You don't have permission to use the command.").builder().color(TextColors.DARK_RED).build());
        }
        return Optional.of(CommandResult.success());
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        List<String> suggestion = new ArrayList<String>();
        suggestion.add("config");
        suggestion.add("config format");
        suggestion.add("config reload");
        return suggestion;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission("extrachat.command.main");
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return Optional.of(Texts.of(description));
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return Optional.of(Texts.of(description));
    }

    @Override
    public Text getUsage(CommandSource source) {
        return Texts.of(usage);
    }
}
