package com.buuz135.extrachat.main.events;


import com.buuz135.extrachat.main.Tag;
import com.buuz135.extrachat.main.config.ConfigLoader;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerChatEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;

import java.util.Random;

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
        mess = blacklistWords(mess);
        event.setMessage(colorString(tag + mess));
    }

    private Text colorString(String mess) {
        return Texts.of(Texts.fromLegacy(mess, '&'));
    }

    private String blacklistWords(String msg) {
        for (String b : ConfigLoader.blacklisted) {
            msg = msg.replaceAll(b, createBlacklistedString(b.length()));
        }
        return msg;
    }

    private String createBlacklistedString(int size) {
        String dot = "";
        String style = "";
        if (ConfigLoader.style == 1) {
            style = "*";
        } else if (ConfigLoader.style == 2) {
            style = "@#%&";
        }
        for (int i = 0; i < size; ++i) {
            dot = dot + getRandomChar(style);
        }
        return dot;
    }

    private char getRandomChar(String s) {
        Random rn = new Random();
        return s.charAt(rn.nextInt(s.length()));
    }
}
