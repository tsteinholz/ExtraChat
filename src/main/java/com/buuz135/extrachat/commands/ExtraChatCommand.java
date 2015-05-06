package com.buuz135.extrachat.commands;


import com.buuz135.extrachat.ExtraChat;
import com.buuz135.extrachat.config.ConfigLoader;
import com.buuz135.extrachat.config.JsonLoader;
import com.buuz135.extrachat.perms.PermsUtils;
import com.google.common.base.Optional;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

import java.util.ArrayList;
import java.util.List;


public class ExtraChatCommand implements CommandCallable {
    private String usage = "USAGE: /ec <help|reload|formatmes|tagadd|tagremove> <format|tag|player> <player>";
    private Object description = "Main command for ExtraChat";
    public static String tag = TextColors.BLUE + "[" + TextColors.DARK_AQUA + "EC" + TextColors.BLUE + "] ";

    @Override
    public Optional<CommandResult> process(CommandSource source, String arguments) throws CommandException {
        String[] args = arguments.split(" ");
        if (args[0].equalsIgnoreCase("help") && args.length == 1) {
            sendHelpMessage(source);
        } else if (args[0].equalsIgnoreCase("reload") && args.length == 1) {
            if (!source.hasPermission(PermsUtils.RELOAD)) {
                source.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                        TextColors.DARK_RED, "You need ", TextColors.YELLOW, PermsUtils.RELOAD, TextColors.DARK_RED,
                        " permission to use that command."));
            } else {
                ConfigLoader.loadConfig();
                source.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                        TextColors.GREEN, "Configuration reloaded."));
            }
        } else if (args[0].equalsIgnoreCase("formatmes")) {
            if (!source.hasPermission(PermsUtils.FORMATMES)) {
                source.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                        TextColors.DARK_RED, "You need ", TextColors.YELLOW, PermsUtils.FORMATMES, TextColors.DARK_RED,
                        " permission to use that command."));
            } else {
                if (args.length == 1) {
                    source.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                            TextColors.DARK_RED, "Please, introduce a valid format."));
                } else {
                    ConfigLoader.saveConfig(arguments.substring(arguments.indexOf("formatMes") + 10), "formatMes");
                    source.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                            TextColors.GREEN, "Format saved."));
                }
            }
        } else if (args[0].equalsIgnoreCase("tagadd")) {
            if (!source.hasPermission(PermsUtils.TAGADD)) {
                source.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                        TextColors.DARK_RED, "You need ", TextColors.YELLOW, PermsUtils.TAGADD, TextColors.DARK_RED,
                        " permission to use that command."));
            } else {
                if (args.length == 3) {
                    if (ExtraChat.game.getServer().getPlayer(args[2]).isPresent()) {
                        JsonLoader.insertTag(args[1], args[2], ExtraChat.game.getServer().getPlayer(args[2]).get().getUniqueId().toString());
                        source.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                                TextColors.GREEN, "Added player " + args[2] + " to the tag " + args[1] + "."));
                    } else {
                        source.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                                TextColors.DARK_RED, "That player its not online."));
                    }
                } else {
                    source.sendMessage(Texts.of(usage).builder().color(TextColors.RED).build());
                }
            }
        } else if (args[0].equalsIgnoreCase("tagremove")) {
            if (!source.hasPermission(PermsUtils.TAGREMOVE)) {
                source.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                        TextColors.DARK_RED, "You need ", TextColors.YELLOW, PermsUtils.TAGREMOVE, TextColors.DARK_RED,
                        " permission to use that command."));
            } else {
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
            }
        } else if (args[0].equalsIgnoreCase("formatTag")) {
            if (!source.hasPermission(PermsUtils.FORMATTAG)) {
                source.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                        TextColors.DARK_RED, "You need ", TextColors.YELLOW, PermsUtils.FORMATTAG, TextColors.DARK_RED,
                        " permission to use that command."));
            } else {
                if (args.length == 1) {
                    source.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                            TextColors.DARK_RED, "Please, introduce a valid format."));
                } else {
                    ConfigLoader.saveConfig(arguments.substring(arguments.indexOf("formatTag") + 11), "formatTag");
                    source.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                            TextColors.GREEN, "Format saved."));
                }
            }
        } else if (args[0].equalsIgnoreCase("addbword")) {
            if (!source.hasPermission(PermsUtils.ADDBWORD)) {
                source.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                        TextColors.DARK_RED, "You need ", TextColors.YELLOW, PermsUtils.ADDBWORD, TextColors.DARK_RED,
                        " permission to use that command."));
            } else {
                if (args.length == 1) {
                    source.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                            TextColors.DARK_RED, "Please, introduce a valid word."));
                } else {
                    ConfigLoader.addWordtoBlackList(arguments.substring(arguments.indexOf("addbword") + 9));
                    source.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                            TextColors.GREEN, "Word added to the blacklist."));
                }
            }
        } else if (args[0].equalsIgnoreCase("removebword")) {
            if (!source.hasPermission(PermsUtils.REMOVEBWORD)) {
                source.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                        TextColors.DARK_RED, "You need ", TextColors.YELLOW, PermsUtils.REMOVEBWORD, TextColors.DARK_RED,
                        " permission to use that command."));
            } else {
                if (args.length == 1) {
                    source.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                            TextColors.DARK_RED, "Please, introduce a valid word."));
                } else {
                    ConfigLoader.removeWordFromBlackList(arguments.substring(arguments.indexOf("removebword") + 12));
                    source.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                            TextColors.GREEN, "Removed word from the blacklist."));
                }
            }
        } else if (args[0].equalsIgnoreCase("togglelog")) {
            if (!source.hasPermission(PermsUtils.TOGGLELOG)) {
                source.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                        TextColors.DARK_RED, "You need ", TextColors.YELLOW, PermsUtils.TOGGLELOG, TextColors.DARK_RED,
                        " permission to use that command."));
            } else {
                ConfigLoader.toggleLog();
                source.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                        TextColors.GREEN, "Toggled the log."));
            }
        } else if (args[0].equalsIgnoreCase("togglereplace")) {
            if (!source.hasPermission(PermsUtils.TOGGLEREPLACE)) {
                source.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                        TextColors.DARK_RED, "You need ", TextColors.YELLOW, PermsUtils.TOGGLEREPLACE, TextColors.DARK_RED,
                        " permission to use that command."));
            } else {
                ConfigLoader.toggleReplace();
                source.sendMessage(Texts.of(TextColors.BLUE, "[", TextColors.DARK_AQUA, "EC", TextColors.BLUE, "] ",
                        TextColors.GREEN, "Toggled the replace function."));
            }
        } else {
            sendHelpMessage(source);
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
        source.sendMessage(Texts.of(TextColors.DARK_AQUA, TextStyles.STRIKETHROUGH, "*****************", TextStyles.RESET, TextStyles.BOLD,
                TextColors.BLUE, " ExtraChat Commands ", TextStyles.RESET, TextColors.DARK_AQUA, TextStyles.STRIKETHROUGH, "*****************"));
        source.sendMessage(Texts.of(TextColors.GREEN, "/ec reload", TextColors.GRAY, TextStyles.BOLD, " -> ", TextStyles.RESET, TextColors.GOLD,
                "Reloads the configuration file."));
        source.sendMessage(Texts.of(TextColors.GREEN, "/ec formatmes <format>", TextColors.GRAY, TextStyles.BOLD, " -> ", TextStyles.RESET, TextColors.GOLD,
                "Saves the message format."));
        source.sendMessage(Texts.of(TextColors.GREEN, "/ec tagadd <tag> <player>", TextColors.GRAY, TextStyles.BOLD, " -> ", TextStyles.RESET, TextColors.GOLD,
                "Adds the player to the tag removing the current tag."));
        source.sendMessage(Texts.of(TextColors.GREEN, "/ec formattag <format>", TextColors.GRAY, TextStyles.BOLD, " -> ", TextStyles.RESET, TextColors.GOLD,
                "Saves the tag format"));
        source.sendMessage(Texts.of(TextColors.GREEN, "/ec tagremove <player>", TextColors.GRAY, TextStyles.BOLD, " -> ", TextStyles.RESET, TextColors.GOLD,
                "Removes the tag from a player"));
    }
}
