package com.buuz135.api;


import com.buuz135.extrachat.config.ConfigLoader;
import com.google.common.base.Optional;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatChannel {


    public static List<ChatChannel> channels = new ArrayList<ChatChannel>();

    public static ChatChannel getChannelWrittingByPlayer(Player pl) {
        for (ChatChannel c : channels) {
            if (c.getPlayersWritting().contains(pl.getUniqueId())) return c;
        }
        return channels.get(0);
    }

    public static void setWrittingChannel(Player player, String channel) {
        for (ChatChannel c : channels) {
            if (c.getName().equalsIgnoreCase(channel) || c.getTag().equalsIgnoreCase(channel)) {
                if (c.getPlayersWritting().contains(player.getUniqueId())) {
                    player.sendMessage(Texts.of(TextColors.DARK_RED, "You are already talking in this channel."));
                    return;
                }
                for (ChatChannel c2 : channels) {
                    if (c2.getPlayersWritting().contains(player.getUniqueId())) {
                        c2.getPlayersWritting().remove(player.getUniqueId());
                    }
                }
                c.getPlayersWritting().add(player.getUniqueId());
                player.sendMessage(Texts.of(TextColors.YELLOW, "You are now talking in the " + c.getName() + " channel."));
                return;
            }
        }
        player.sendMessage(Texts.of(TextColors.DARK_RED, "No channel found with that name."));
    }

    public static void setPlayerListening(Player player, String channel, Optional<String> pass, Game game) {
        for (ChatChannel c : channels) {
            if (c.getName().equalsIgnoreCase(channel) || c.getTag().equalsIgnoreCase(channel)) {
                if (c.getPlayersListening().contains(player.getUniqueId())) {
                    player.sendMessage(Texts.of(TextColors.DARK_RED, "You are already in this channel."));
                    return;
                }
                if (c.isPrivate() && !pass.isPresent()) {
                    player.sendMessage(Texts.of(TextColors.DARK_RED, "This channel needs a password."));
                    return;
                }
                if (c.isPrivate() && !pass.get().equals(c.getPass())) {
                    player.sendMessage(Texts.of(TextColors.DARK_RED, "That password is not correct."));
                    return;
                }
                for (UUID id : c.getPlayersListening()) {
                    game.getServer().getPlayer(id).get().sendMessage(Texts.of(TextColors.GOLD,
                            Format.formatChannelTag(ConfigLoader.channelFormat, c.getTag()), " Player " + player.getName() + " joined the channel."));
                }
                c.getPlayersListening().add(player.getUniqueId());
                setWrittingChannel(player, channel);
                return;
            }
        }
        player.sendMessage(Texts.of(TextColors.DARK_RED, "No channel found with that name."));
    }

    public static void removePlayerListening(Player player, String channel, Game game) {
        for (ChatChannel c : channels) {
            if (c.getName().equalsIgnoreCase(channel) || c.getTag().equalsIgnoreCase(channel)) {
                if (c.getPlayersListening().contains(player.getUniqueId())) {
                    c.getPlayersListening().remove(player.getUniqueId());
                    player.sendMessage(Texts.of(TextColors.YELLOW, "You left the " + c.getName() + " channel."));
                    for (UUID id : c.getPlayersListening()) {
                        game.getServer().getPlayer(id).get().sendMessage(Texts.of(TextColors.GOLD,
                                Format.formatChannelTag(ConfigLoader.channelFormat, c.getTag()), " Player " + player.getName() + " left the channel."));
                    }
                    return;
                }
            }
        }
        player.sendMessage(Texts.of(TextColors.DARK_RED, "No channel found with that name."));
    }

    private String name;
    private String tag;
    private int radius;
    private boolean isDefault;
    private boolean isPrivate;
    private String pass;
    private List<UUID> playersListening;
    private List<UUID> playersWritting;

    public ChatChannel(String name, String tag, int radius, boolean isDefault, boolean isPrivate, String pass) {
        this.name = name;
        this.tag = tag;
        this.radius = radius;
        this.isPrivate = isPrivate;
        this.isDefault = isDefault;
        this.pass = pass;
        playersListening = new ArrayList<UUID>();
        playersWritting = new ArrayList<UUID>();
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<UUID> getPlayersListening() {
        return playersListening;
    }

    public List<UUID> getPlayersWritting() {
        return playersWritting;
    }
}
