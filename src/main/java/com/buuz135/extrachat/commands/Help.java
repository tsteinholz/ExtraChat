package com.buuz135.extrachat.commands;


import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.action.HoverAction;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class Help implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        source.sendMessage(Texts.of(TextColors.DARK_AQUA, TextStyles.STRIKETHROUGH, "*****************", TextStyles.RESET, TextStyles.BOLD,
                TextColors.BLUE, " ExtraChat Commands ", TextStyles.RESET, TextColors.DARK_AQUA, TextStyles.STRIKETHROUGH, "*****************"));
        source.sendMessage(Texts.of(TextColors.GREEN, "/ec reload").builder().onHover(new HoverAction.ShowText(Texts.of(TextColors.GOLD,
                "Reloads the configuration file."))).build());
        source.sendMessage(Texts.of(TextColors.GREEN, "/ec formatmes <format>").builder().onHover(new HoverAction.ShowText(Texts.of(TextColors.GOLD,
                "Saves the message format."))).build());
        source.sendMessage(Texts.of(TextColors.GREEN, "/ec tagadd <tag> <player>").builder().onHover(new HoverAction.ShowText(Texts.of(TextColors.GOLD,
                "Adds the player to the tag removing the current tag."))).build());
        source.sendMessage(Texts.of(TextColors.GREEN, "/ec formattag <format>").builder().onHover(new HoverAction.ShowText(Texts.of(TextColors.GOLD,
                "Saves the tag format."))).build());
        source.sendMessage(Texts.of(TextColors.GREEN, "/ec tagremove <player>").builder().onHover(new HoverAction.ShowText(Texts.of(TextColors.GOLD,
                "Removes the tag from the player."))).build());
        source.sendMessage(Texts.of(TextColors.GREEN, "/ec togglereplace").builder().onHover(new HoverAction.ShowText(Texts.of(TextColors.GOLD,
                "Toggles the replace function."))).build());
        source.sendMessage(Texts.of(TextColors.GREEN, "/ec togglelog").builder().onHover(new HoverAction.ShowText(Texts.of(TextColors.GOLD,
                "Toggles the log."))).build());
        return CommandResult.success();
    }
}
