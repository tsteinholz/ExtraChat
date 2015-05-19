package com.buuz135.extrachat.events;


import com.buuz135.api.ChatChannel;
import com.buuz135.api.Format;
import com.buuz135.extrachat.config.ConfigLoader;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerJoinEvent;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

import java.util.UUID;

public class PlayerJoin {
    @Subscribe
    public void playerJoin(PlayerJoinEvent event) {
        for (ChatChannel c : ChatChannel.channels) {
            if (c.isDefault()) {
                if (!c.getPlayersListening().contains(event.getEntity().getUniqueId())) {
                    c.getPlayersListening().add(event.getEntity().getUniqueId());
                }
                if (ConfigLoader.announce) {
                    for (UUID id : c.getPlayersListening()) {
                        if (event.getGame().getServer().getPlayer(id).isPresent()) {
                            event.getGame().getServer().getPlayer(id).get().sendMessage(Texts.of(TextColors.GOLD,
                                    Format.formatChannelTag(ConfigLoader.channelFormat, c.getTag()), " Player " + event.getEntity().getName() + " joined the channel."));
                        }
                    }
                }
            }
        }
    }
}
