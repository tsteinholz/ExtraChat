package com.buuz135.api.blacklist;


import org.spongepowered.api.event.entity.player.PlayerChatEvent;
import org.spongepowered.api.text.Texts;

import java.util.ArrayList;
import java.util.List;

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

    public void execute(PlayerChatEvent event) {
        if (Texts.toPlain(event.getNewMessage()).replaceAll(regexFilter,"").length() == Texts.toPlain(event.getNewMessage()).length())return;
        if (action.equals(WordAction.KICK)) {
            event.setCancelled(cancel);
            event.setNewMessage(Texts.of(""));
            if (alert != null) {
                event.getGame().getServer().broadcastMessage(Texts.fromLegacy(alert.replaceAll("%PLAYER%",event.getEntity().getName()), '&'));
            }
            event.getGame().getCommandDispatcher().process(event.getGame().getServer().getConsole(),"kick " + event.getEntity().getName() + " " + privateMessage);//TODO Implement when implemented
        }
    }
}
