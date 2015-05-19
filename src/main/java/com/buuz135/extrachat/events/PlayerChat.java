package com.buuz135.extrachat.events;


import com.buuz135.api.ChatChannel;
import com.buuz135.api.Format;
import com.buuz135.api.Tag;
import com.buuz135.extrachat.ExtraChat;
import com.buuz135.extrachat.config.ConfigLoader;
import org.spongepowered.api.data.manipulator.DisplayNameData;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerChatEvent;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.world.Location;

import java.util.UUID;

public class PlayerChat {
    @Subscribe
    public void onChat(PlayerChatEvent event) {
        String tag = null;
        String name = "";
        for (Tag temp : Tag.tags) {
            if (temp.getPlayers().containsKey(event.getEntity().getUniqueId())) {
                tag = ConfigLoader.formatTag.replace("%TAG%", temp.getName());
            }
        }
        if (tag == null) {
            for (Tag temp : Tag.tags) {
                if (temp.isDefault()) {
                    tag = ConfigLoader.formatTag.replace("%TAG%", temp.getName());
                }
            }
        }
        if (!event.getEntity().getData(DisplayNameData.class).isPresent() ||
                Texts.toPlain(event.getEntity().getData(DisplayNameData.class).get().getDisplayName()).equals("")) {
            name = event.getEntity().getName();
        } else if (event.getEntity().getData(DisplayNameData.class).isPresent()) {
            name = Texts.toLegacy(event.getEntity().getData(DisplayNameData.class).get().getDisplayName(), '&');
        }
        String mess = Format.formatMessageToString(ConfigLoader.formatMes, name, Format.getRawMessage(Texts.toLegacy(event.getMessage(), '&')));
        String style = "*";
        if (ConfigLoader.style == 2) {
            style = "@#*%&";
        }
        mess = Format.blacklistWords(mess, ConfigLoader.blacklisted, style);
        if (Format.getRawMessage(Texts.toPlain(event.getMessage())).startsWith("r/") && ConfigLoader.replaceEnabled) {
            ExtraChat.replaceLogger.fixMsg(event.getEntity().getName(), Format.getRawMessage(Texts.toLegacy(event.getMessage(), '&')));
            event.setCancelled(true);
        } else {
            ExtraChat.replaceLogger.insertLog(event.getEntity().getName(), Format.getRawMessage(Texts.toLegacy(event.getMessage(), '&')));
            event.setNewMessage(Format.colorString("&r" + tag + mess));
        }
        event.setCancelled(ConfigLoader.chatChannels);
    }
    @Subscribe(order = Order.POST, ignoreCancelled = false)
    public void onChannelChat(PlayerChatEvent event){
        if (!ConfigLoader.chatChannels) return;
        ChatChannel c = ChatChannel.getChannelWrittingByPlayer(event.getEntity());
        for (UUID id : c.getPlayersListening()){
            if (event.getGame().getServer().getPlayer(id).isPresent()){
                ExtraChat.logger.info(distance(event.getEntity().getLocation(),event.getGame().getServer().getPlayer(id).get().getLocation())+"");
               if (c.getRadius() == -1 || (event.getEntity().getWorld() == event.getGame().getServer().getPlayer(id).get().getWorld() &&
               distance(event.getEntity().getLocation(), event.getGame().getServer().getPlayer(id).get().getLocation())<=c.getRadius())){
                   event.getGame().getServer().getPlayer(id).get().sendMessage(Format.
                           formatChannelTag(ConfigLoader.channelFormat,c.getTag()).builder().append(event.getNewMessage()).build());
               }
            }
        }
    }
    private double distance(Location loc1, Location loc2){
        return Math.sqrt(Math.pow(loc1.getX()-loc2.getX(),2) + Math.pow(loc1.getY()- loc2.getY(),2)+Math.pow(loc1.getZ()-loc2.getZ(),2));
    }
}
