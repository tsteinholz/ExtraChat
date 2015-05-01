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
    private String usage = "USAGE: /ec <help|reload|formatmes|tagadd|tagremove> <format|tag|player> <player>";
    private Object description = "Main command for ExtraChat";

    @Override
    public Optional<CommandResult> process(CommandSource source, String arguments) throws CommandException {
        if (testPermission(source)) {
            String[] args = arguments.split(" ");
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("help") && args.length == 1) {
                    sendHelpMessage(source);
                } else if (args[0].equalsIgnoreCase("reload") && args.length == 1) {
                    ConfigLoader.loadConfig();
                    source.sendMessage(Texts.of("Configuration reloaded.").builder()
                            .color(TextColors.GREEN).build());
                } else if (args[0].equalsIgnoreCase("formatmes")) {
                    if (args.length == 1) {
                        source.sendMessage(Texts.of("Please, introduce a valid format.").builder()
                                .color(TextColors.DARK_RED).build());
                    } else {
                        ConfigLoader.saveConfig(arguments.substring(arguments.indexOf("formatMes") + 10), "formatMes");
                        source.sendMessage(Texts.of("Format saved.").builder().color(TextColors.GREEN)
                                .build());
                    }
                } else if (args[0].equalsIgnoreCase("tagadd")) {
                    if (args.length == 3) {
                        if (ExtraChat.game.getServer().getPlayer(args[2]).isPresent()) {
                            JsonLoader.insertTag(args[1], args[2], ExtraChat.game.getServer().getPlayer(args[2]).get().getUniqueId().toString());
                            source.sendMessage(Texts.of("Added player " + args[2] + " to the tag " + args[1] + ".").builder().color(TextColors.GREEN)
                                    .build());
                        } else {
                            source.sendMessage(Texts.of("That player its not online.").builder().color(TextColors.RED).build());
                        }
                    } else {
                        source.sendMessage(Texts.of(usage).builder().color(TextColors.RED).build());
                    }
                } else if (args[0].equalsIgnoreCase("tagremove")) {
                    if (args.length == 2) {
                        if (ExtraChat.game.getServer().getPlayer(args[1]).isPresent()) {
                            JsonLoader.removeTag(ExtraChat.game.getServer().getPlayer(args[1]).get().getUniqueId().toString());
                            source.sendMessage(Texts.of("Removed tag from the player " + args[1] + ".").builder().color(TextColors.GREEN)
                                    .build());
                        } else {
                            source.sendMessage(Texts.of("That player its not online.").builder().color(TextColors.RED).build());
                        }
                    } else {
                        source.sendMessage(Texts.of(usage).builder().color(TextColors.RED).build());
                    }
                } else if (args[0].equalsIgnoreCase("formatTag")) {
                    if (args.length == 1) {
                        source.sendMessage(Texts.of("Please, introduce a valid format.").builder()
                                .color(TextColors.DARK_RED).build());
                    } else {
                        ConfigLoader.saveConfig(arguments.substring(arguments.indexOf("formatTag") + 11), "formatTag");
                        source.sendMessage(Texts.of("Format saved.").builder().color(TextColors.GREEN)
                                .build());
                    }
                } else if (args[0].equalsIgnoreCase("addbword")) {
                    if (args.length == 1) {
                        source.sendMessage(Texts.of("Please, introduce a valid word.").builder()
                                .color(TextColors.DARK_RED).build());
                    } else {
                        ConfigLoader.addWordtoBlackList(arguments.substring(arguments.indexOf("addbword") + 9));
                        source.sendMessage(Texts.of("Word added to the blacklist.").builder().color(TextColors.GREEN)
                                .build());
                    }
                } else if (args[0].equalsIgnoreCase("removebword")) {
                    if (args.length == 1) {
                        source.sendMessage(Texts.of("Please, introduce a valid word.").builder()
                                .color(TextColors.DARK_RED).build());
                    } else {
                        ConfigLoader.removeWordFromBlackList(arguments.substring(arguments.indexOf("removebword") + 12));
                        source.sendMessage(Texts.of("Removed word from the blacklist.").builder().color(TextColors.GREEN)
                                .build());
                    }
                } else if (args[0].equalsIgnoreCase("togglelog")) {
                    ConfigLoader.toggleLog();
                    source.sendMessage(Texts.of("Toggled the log.").builder().color(TextColors.GREEN)
                            .build());
                } else if (args[0].equalsIgnoreCase("togglereplace")) {
                    ConfigLoader.toggleReplace();
                    source.sendMessage(Texts.of("Toggled the replace function.").builder().color(TextColors.GREEN)
                            .build());
                } else {
                    sendHelpMessage(source);
                }
            }
        } else {
            source.sendMessage(Texts.of("You don't have permission to use the command.").builder().color(TextColors.DARK_RED).build());
        }
        return Optional.of(CommandResult.success());
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        List<String> suggestion = new ArrayList<String>();
        suggestion.add("reload");
        suggestion.add("formatmes");
        suggestion.add("tagadd");
        suggestion.add("help");
        suggestion.add("formattag");
        suggestion.add("tagremove");
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

    private void sendHelpMessage(CommandSource source) {
        source.sendMessage(Texts.of("******************").builder().color(TextColors.DARK_AQUA)
                .append(Texts.of(" ExtraChat Commands ").builder().color(TextColors.BLUE).build())
                .append(Texts.of("******************").builder().color(TextColors.DARK_AQUA).build()).build());
        source.sendMessage(Texts.of("/ec reload").builder().color(TextColors.GREEN).append(Texts.of(" -> ")
                .builder().color(TextColors.GRAY).build()).append(Texts.of("Reloads the configuration file.")
                .builder().color(TextColors.GOLD).build()).build());
        source.sendMessage(Texts.of("/ec formatmes <format>").builder().color(TextColors.GREEN).append(Texts.of(" -> ")
                .builder().color(TextColors.GRAY).build()).append(Texts.of("Saves the message format.")
                .builder().color(TextColors.GOLD).build()).build());
        source.sendMessage(Texts.of("/ec tagadd <tag> <player>").builder().color(TextColors.GREEN).append(Texts.of(" -> ")
                .builder().color(TextColors.GRAY).build()).append(Texts.of("Adds the player to the tag removing the current tag.")
                .builder().color(TextColors.GOLD).build()).build());
        source.sendMessage(Texts.of("/ec formattag <format>").builder().color(TextColors.GREEN).append(Texts.of(" -> ")
                .builder().color(TextColors.GRAY).build()).append(Texts.of("Saves the tag format.")
                .builder().color(TextColors.GOLD).build()).build());
        source.sendMessage(Texts.of("/ec tagremove <player>").builder().color(TextColors.GREEN).append(Texts.of(" -> ")
                .builder().color(TextColors.GRAY).build()).append(Texts.of("Removes the current tag form the player.")
                .builder().color(TextColors.GOLD).build()).build());
    }
}
