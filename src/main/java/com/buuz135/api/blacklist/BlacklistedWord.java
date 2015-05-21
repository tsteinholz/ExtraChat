package com.buuz135.api.blacklist;


import com.buuz135.api.Format;
import com.buuz135.extrachat.ExtraChat;
import org.spongepowered.api.event.entity.player.PlayerChatEvent;
import org.spongepowered.api.text.Texts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlacklistedWord {

    public static List<BlacklistedWord> blacklistedWordList = new ArrayList<BlacklistedWord>();

    private List<String> wordsReplace;
    private String regexFilter;
    private WordAction action;
    private String privateMessage;
    private boolean cancel;
    private String alert;

    public BlacklistedWord(String privateMessage, WordAction action, String regexFilter, boolean cancel, String alert) {
        this.privateMessage = privateMessage;
        this.action = action;
        this.cancel = cancel;
        this.alert = alert;
        this.regexFilter = regexFilter;
        wordsReplace = new ArrayList<String>();
    }

    public boolean hasPrivateMessage() {
        return privateMessage != null;
    }

    public String getRegexFilter() {
        return regexFilter;
    }

    public void setRegexFilter(String regexFilter) {
        this.regexFilter = regexFilter;
    }

    public WordAction getAction() {
        return action;
    }

    public void setAction(WordAction action) {
        this.action = action;
    }

    public String getPrivateMessage() {
        return privateMessage;
    }

    public void setPrivateMessage(String privateMessage) {
        this.privateMessage = privateMessage;
    }

    public List<String> getWordsReplace() {
        return wordsReplace;
    }

    public void execute(PlayerChatEvent event) {
        if (Texts.toPlain(event.getNewMessage()).replaceAll(regexFilter, "").equals(Texts.toPlain(event.getNewMessage())))
        return;
        if (action.equals(WordAction.KICK)) {
            event.setCancelled(cancel);
            event.setNewMessage(Texts.of(""));
            if (alert != null) {
                event.getGame().getServer().broadcastMessage(Texts.fromLegacy(alert.replaceAll("%PLAYER%", event.getEntity().getName()), '&'));
            }
            event.getGame().getCommandDispatcher().process(event.getGame().getServer().getConsole(), "kick " + event.getEntity().getName() + " " + privateMessage);//TODO Implement when implemented
        }
        if (action.equals(WordAction.COLOR)) {
            event.setNewMessage(Texts.fromLegacy(Texts.toLegacy(event.getNewMessage(), '&').replaceAll(regexFilter, privateMessage), '&'));
        }
        if (action.equals(WordAction.REPLACE)) {
            ExtraChat.logger.info("REPLACE");
            Random rn = new Random();
            event.setNewMessage(Texts.fromLegacy(Texts.toLegacy(event.getNewMessage(), '&').replaceAll(regexFilter, wordsReplace.get(rn.nextInt(wordsReplace.size()))), '&'));
        }
        if (action.equals(WordAction.STRIKEOUT)){
            String filter = Format.createBlacklistedString(Texts.toPlain(event.getNewMessage()).length() - Texts.toPlain(event.getNewMessage()).replaceAll(regexFilter, "").length(), privateMessage);
            event.setNewMessage(Texts.fromLegacy(Texts.toLegacy(event.getNewMessage(), '&').replaceAll(regexFilter, filter), '&'));
        }
    }
}
