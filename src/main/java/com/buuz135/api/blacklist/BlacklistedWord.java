package com.buuz135.api.blacklist;


import com.buuz135.api.Format;
import org.spongepowered.api.event.entity.player.PlayerChatEvent;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.TextMessageException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class BlacklistedWord {

    public static List<BlacklistedWord> blacklistedWordList = new ArrayList<BlacklistedWord>();

    private List<String> wordsReplace;
    private List<String> regexFilter;
    private WordAction action;
    private String privateMessage;
    private boolean cancel;
    private String alert;

    public BlacklistedWord(String privateMessage, WordAction action, List<String> regexFilter, boolean cancel, String alert) {
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

    public List<String> getRegexFilter() {
        return regexFilter;
    }

    public void setRegexFilter(List<String> regexFilter) {
        this.regexFilter = regexFilter;
    }

    public List<String> getWordsReplace() {
        return wordsReplace;
    }

    public void execute(PlayerChatEvent event) throws TextMessageException {
        for (String actualFilter : regexFilter) {
            if (Pattern.compile(actualFilter, Pattern.CASE_INSENSITIVE).matcher(Texts.toPlain(event.getNewMessage())).find()) {
                if (action.equals(WordAction.KICK)) {
                    event.setCancelled(cancel);
                    if (cancel) event.setNewMessage(Texts.of(""));
                    if (alert != null) {
                        event.getGame().getServer().getBroadcastSink().sendMessage(Texts.legacy().from(alert.replaceAll("%PLAYER%", event.getEntity().getName())));
                    }
                    event.getGame().getCommandDispatcher().process(event.getGame().getServer().getConsole(), "kick " + event.getEntity().getName() + " " + privateMessage);//TODO Implement when implemented
                }
                if (action.equals(WordAction.COLOR)) {
                    event.setNewMessage(Texts.legacy().from(Texts.legacy().to(event.getNewMessage()).replaceAll(actualFilter, privateMessage).replaceAll("&", "" + Texts.getLegacyChar())));
                }
                if (action.equals(WordAction.REPLACE)) {
                    Random rn = new Random();
                    event.setNewMessage(Texts.legacy().from(Texts.legacy().to(event.getNewMessage()).replaceAll(actualFilter, wordsReplace.get(rn.nextInt(wordsReplace.size())))));
                }
                if (action.equals(WordAction.STRIKEOUT)) {
                    String filter = Format.createBlacklistedString(Texts.toPlain(event.getNewMessage()).length() - Texts.toPlain(event.getNewMessage()).replaceAll(actualFilter, "").length(), privateMessage);
                    event.setNewMessage(Texts.legacy().from(Texts.legacy().to(event.getNewMessage()).replaceAll(actualFilter, filter)));
                }
                if (action.equals(WordAction.COMMAND)) {
                    event.setCancelled(cancel);
                    if (cancel) event.setNewMessage(Texts.of(""));
                    if (privateMessage != null) event.getEntity().sendMessage(Texts.legacy().from(privateMessage));
                    event.getGame().getCommandDispatcher().process(event.getGame().getServer().getConsole(), alert.replaceAll("%PLAYER%", event.getEntity().getName()));
                }
            }
        }
    }
}
