package com.buuz135.extrachat.main.events;


import com.buuz135.extrachat.main.Tag;
import com.buuz135.extrachat.main.config.ConfigLoader;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerChatEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;

public class PlayerChat {
    @Subscribe
    public void onChat(PlayerChatEvent event) {
        String tag = "";
        for (Tag temp : Tag.tags) {
            if (temp.getPlayers().containsKey(event.getPlayer().getUniqueId())) {
                tag = ConfigLoader.formatTag.replace("%TAG%", temp.getName());
            }
        }
        String mess = Texts.toPlain(event.getMessage());
        mess = ConfigLoader.formatMes.replace("%PLAYER%", event.getPlayer().getName())
                .replace("%MES%", mess.substring(mess.indexOf(" ") + 1));


        event.setMessage(colorString(tag + mess));
    }

    private Text colorString(String mess) {
        return Texts.of(Texts.replaceCodes(mess, '&'));
    }
}
