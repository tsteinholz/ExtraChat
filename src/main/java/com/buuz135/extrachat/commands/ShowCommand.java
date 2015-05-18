package com.buuz135.extrachat.commands;


import com.buuz135.extrachat.ExtraChat;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.action.HoverAction;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class ShowCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        if (ExtraChat.game.getServer().getPlayer(source.getName()).get().getItemInHand().isPresent()) {
            ExtraChat.game.getServer().broadcastMessage(Texts.of(TextStyles.BOLD,source.getName(),TextStyles.RESET," shows this ").builder()
                    .append(Texts.of(TextColors.DARK_AQUA, "item",TextColors.WHITE,".").builder().onHover(new HoverAction.ShowItem(ExtraChat.game.getServer()
                            .getPlayer(source.getName()).get().getItemInHand().get())).build()).build());
        } else {
            source.sendMessage(Texts.of(TextColors.DARK_RED, "You don't have an item in your hand."));
        }
        return CommandResult.success();
    }
}
