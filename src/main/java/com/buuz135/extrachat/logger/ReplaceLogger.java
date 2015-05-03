package com.buuz135.extrachat.main.logger;


import com.buuz135.extrachat.api.Format;
import com.buuz135.extrachat.main.ExtraChat;
import com.buuz135.extrachat.main.config.ConfigLoader;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.Iterator;
import java.util.Map;

public class ReplaceLogger {

    private Multimap<String, String> log;
    private Multimap<String, String> logTemp;

    public ReplaceLogger() {
        log = ArrayListMultimap.create();
    }

    public void insertLog(String player, String msg) {
        log.put(player, msg);
        if (log.size() > ConfigLoader.replaceInt) {
            log.remove(log.entries().iterator().next().getKey(), log.entries().iterator().next().getValue());
        }
    }

    public void fixMsg(String player, String fix) {
        String[] args = fix.split("/");
        if (args.length == 3) {
            logTemp = ArrayListMultimap.create();
            recursive(log.entries().iterator(), player, args[1], args[2], true);
            write(logTemp.entries().iterator());
        }
    }

    private boolean recursive(Iterator<Map.Entry<String, String>> iterator, String player, String toFix, String fix, boolean isFixedNeed) {
        Map.Entry<String, String> entry = iterator.next();
        String name = entry.getKey();
        String msg = entry.getValue();
        boolean replaced = false;
        if (iterator.hasNext()) {
            replaced = recursive(iterator, player, toFix, fix, isFixedNeed);
        }
        if (!replaced && name.equals(player) && msg.contains(toFix) && isFixedNeed) {
            msg = msg.replaceAll(toFix, fix);
            replaced = true;
        }
        logTemp.put(name, msg);
        return replaced;
    }

    private void write(Iterator<Map.Entry<String, String>> iterator) {
        Map.Entry<String, String> entry = iterator.next();
        if (iterator.hasNext()) write(iterator);
        ExtraChat.game.getServer().broadcastMessage(Format.colorString(Format.formatMessageToString(ConfigLoader.formatMes, entry.getKey(), entry.getValue())));
        insertLog(entry.getKey(), entry.getValue());
    }
}
