package com.buuz135.extrachat.commands;


import com.buuz135.extrachat.broadcast.BRCommand;
import com.buuz135.extrachat.perms.PermsUtils;
import org.spongepowered.api.Game;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandExecutor;
import org.spongepowered.api.util.command.spec.CommandSpec;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CommandRegister {

    public static void registerCommands(Game game, Object plugin) {
        HashMap<List<String>, CommandSpec> subcommands = new HashMap<List<String>, CommandSpec>();
        CommandSpec reload = CommandSpec.builder().description(Texts.of(TextColors.GOLD,
                "Reloads the configuration file.")).permission(PermsUtils.RELOAD).executor(new Reload()).build();
        CommandSpec help = CommandSpec.builder().description(Texts.of(TextColors.GOLD,
                "Help command.")).executor(new Help()).build();
        CommandSpec formatmes = CommandSpec.builder().description(Texts.of(TextColors.GOLD,
                "Saves the message format.")).permission(PermsUtils.FORMATMES).executor(new FormatMessage())
                .arguments(GenericArguments.remainingJoinedStrings(Texts.of("format"))).build();
        CommandSpec tagadd = CommandSpec.builder().description(Texts.of(TextColors.GOLD,
                "Adds the player to the tag removing the current tag.")).permission(PermsUtils.TAGADD).
                executor(new TaggAdd()).arguments(GenericArguments.seq(GenericArguments.player(Texts.of("player"), game),
                GenericArguments.remainingJoinedStrings(Texts.of("tag")))).build();
        CommandSpec tagremove = CommandSpec.builder().description(Texts.of(TextColors.GOLD,
                "Removes the current tag of a player.")).permission(PermsUtils.TAGREMOVE).executor(new TagRemove())
                .arguments(GenericArguments.player(Texts.of("player"), game)).build();
        CommandSpec formattag = CommandSpec.builder().description(Texts.of(TextColors.GOLD,
                "Saves the tag format.")).permission(PermsUtils.FORMATTAG).executor(new FormatTag())
                .arguments(GenericArguments.remainingJoinedStrings(Texts.of("format"))).build();
        CommandSpec togglelog = CommandSpec.builder().description(Texts.of(TextColors.GOLD,
                "Toggles the log.")).executor(new ToggleLog()).permission(PermsUtils.TOGGLELOG).build();
        CommandSpec togglereplace = CommandSpec.builder().description(Texts.of(TextColors.GOLD,
                "Toggles the place function.")).executor(new ToggleReplace()).permission(PermsUtils.TOGGLEREPLACE).build();

        subcommands.put(Arrays.asList("reload"), reload);
        subcommands.put(Arrays.asList("help"), help);
        subcommands.put(Arrays.asList("formatmes"), formatmes);
        subcommands.put(Arrays.asList("tagadd"), tagadd);
        subcommands.put(Arrays.asList("tagremove"), tagremove);
        subcommands.put(Arrays.asList("formattag"), formattag);
        subcommands.put(Arrays.asList("togglelog"), togglelog);
        subcommands.put(Arrays.asList("togglereplace"),togglereplace);

        CommandSpec main = CommandSpec.builder().description(Texts.of(TextColors.GOLD,
                "Main ExtraChat command.")).permission(PermsUtils.MAIN).executor(new CommandExecutor() {
            @Override
            public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
                return CommandResult.empty();
            }
        }).children(subcommands).build();
        CommandSpec broadcast = CommandSpec.builder().description(Texts.of(TextColors.GOLD,
                "Broadcast a message to all the players.")).permission(PermsUtils.BR).executor(new BRCommand())
                .arguments(GenericArguments.remainingJoinedStrings(Texts.of("broadcast"))).build();
        CommandSpec show = CommandSpec.builder().description(Texts.of(TextColors.GOLD,
                "Shows the current item in your hand to the rest of the players.")).permission(PermsUtils.SHOW).executor(new ShowCommand())
                .build();
        CommandSpec privateMes = CommandSpec.builder().description(Texts.of(TextColors.GOLD,
                "Sends a private message to the player")).executor(new PrivateMessage(game))
                .arguments(GenericArguments.player(Texts.of("playerDest"), game), GenericArguments.remainingJoinedStrings(Texts.of("message"))).build();
        CommandSpec reply = CommandSpec.builder().description(Texts.of(TextColors.GOLD,
                "Replys the last message")).arguments(GenericArguments.remainingJoinedStrings(Texts.of("message")))
                .executor(new ReplyPrivate(game)).build();

        game.getCommandDispatcher().register(plugin, main, "ec", "extrachat");
        game.getCommandDispatcher().register(plugin,broadcast,"br","ecbr","broadcast");
        game.getCommandDispatcher().register(plugin,show,"show");
        game.getCommandDispatcher().register(plugin,privateMes,"tell","w","msg");
        game.getCommandDispatcher().register(plugin,reply,"r","reply");
    }
}
