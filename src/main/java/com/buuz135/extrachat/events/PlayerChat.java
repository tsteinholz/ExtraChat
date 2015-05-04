package main.java.com.buuz135.extrachat.events;


import main.java.com.buuz135.api.Format;
import main.java.com.buuz135.extrachat.ExtraChat;
import main.java.com.buuz135.extrachat.Tag;
import main.java.com.buuz135.extrachat.config.ConfigLoader;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerChatEvent;
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
        String mess = Format.formatMessageToString(ConfigLoader.formatMes, event.getPlayer().getName(), Format.getRawMessage(Texts.toPlain(event.getMessage())));
        String style = "*";
        if (ConfigLoader.style == 2) {
            style = "@#*%&";
        }
        mess = Format.blacklistWords(mess, ConfigLoader.blacklisted, style);
        if (Format.getRawMessage(Texts.toPlain(event.getMessage())).startsWith("r/") && ConfigLoader.replaceEnabled) {
            ExtraChat.replaceLogger.fixMsg(event.getPlayer().getName(), Format.getRawMessage(Texts.toPlain(event.getMessage())));
            event.setCancelled(true);
        } else {
            ExtraChat.replaceLogger.insertLog(event.getPlayer().getName(), Format.getRawMessage(Texts.toPlain(event.getMessage())));
            event.setMessage(Format.colorString("&r" + tag + mess));
        }
    }
}
