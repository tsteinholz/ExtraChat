package com.buuz135.extrachat.events;


import com.buuz135.api.Format;
import com.buuz135.extrachat.ExtraChat;
import com.buuz135.extrachat.Tag;
import com.buuz135.extrachat.config.ConfigLoader;
import org.spongepowered.api.data.manipulator.DisplayNameData;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerChatEvent;
import org.spongepowered.api.text.Texts;

public class PlayerChat {
    @Subscribe
    public void onChat(PlayerChatEvent event) {
        event.getMessage();
        String tag = "";
        String name = "";
        for (Tag temp : Tag.tags) {
            if (temp.getPlayers().containsKey(event.getEntity().getUniqueId())) {
                tag = ConfigLoader.formatTag.replace("%TAG%", temp.getName());
            }
        }
        if (!event.getEntity().getData(DisplayNameData.class).isPresent() ||
                Texts.toPlain(event.getEntity().getData(DisplayNameData.class).get().getDisplayName()).equals("")) {
            name = event.getEntity().getName();
        } else if (event.getEntity().getData(DisplayNameData.class).isPresent()){
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
    }
}
