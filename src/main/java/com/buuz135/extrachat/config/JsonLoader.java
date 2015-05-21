package com.buuz135.extrachat.config;


import com.buuz135.api.ChatChannel;
import com.buuz135.api.Tag;
import com.buuz135.api.blacklist.BlacklistedWord;
import com.buuz135.api.blacklist.WordAction;
import com.buuz135.extrachat.ExtraChat;
import com.buuz135.extrachat.broadcast.Broadcaster;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.action.ClickAction;
import org.spongepowered.api.text.action.HoverAction;

import java.io.*;
import java.net.URL;
import java.util.Iterator;
import java.util.UUID;

public class JsonLoader {
    private static String path = "config" + File.separator + "ExtraChat" + File.separator;
    private static File tagsjson = new File(path + "tags.json");
    private static File broadcastjson = new File(path + "broadcasts.json");
    private static File chatGroups = new File(path + "chatGroups.json");
    private static File blacklistjson = new File(path + "blacklist.json");

    public static void initJson() {
        try {
            if (!tagsjson.exists()) {
                tagsjson.createNewFile();
            }
            readTagsJson();
            if (!broadcastjson.exists()) {
                broadcastjson.createNewFile();
            }
            readBroadcasts();
            if (!chatGroups.exists() && ConfigLoader.chatChannels) {
                chatGroups.createNewFile();
                ChatChannel.channels.add(new ChatChannel("global", "G", -1, true, false, ""));
                ChatChannel.channels.add(new ChatChannel("local", "L", 200, true, false, ""));
                updateChatGroups();

            } else if (ConfigLoader.chatChannels) readChatGroups();
            if (!blacklistjson.exists()) {
                blacklistjson.createNewFile();
            }
            readBlacklist();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void insertTag(String tag, String name, String UUID) {
        JsonWriter writer = null;
        for (Tag t : Tag.tags) {
            if (t.getPlayers().containsKey(java.util.UUID.fromString(UUID))) {
                t.getPlayers().remove(java.util.UUID.fromString(UUID));
                break;
            }
        }
        Tag.getTagByName(tag).getPlayers().put(java.util.UUID.fromString(UUID), name);
        updateTags();
    }

    public static void readTagsJson() {
        try {
            JsonArray array = new JsonParser().parse(new JsonReader(new FileReader(tagsjson))).getAsJsonArray();
            Iterator<JsonElement> it = array.iterator();
            while (it.hasNext()) {
                JsonObject object = it.next().getAsJsonObject();
                Tag t = new Tag(object.get("name").getAsString(), object.get("default").getAsBoolean());
                if (!object.get("default").getAsBoolean()) {
                    Iterator<JsonElement> it2 = object.getAsJsonArray("players").iterator();
                    while (it2.hasNext()) {
                        JsonObject player = it2.next().getAsJsonObject();
                        t.getPlayers().put(UUID.fromString(player.get("playerUUID").getAsString()),
                                player.get("playerName").getAsString());
                    }
                }
                Tag.tags.add(t);
            }
            int numDef = 0;
            for (Tag t : Tag.tags) {
                if (t.isDefault()) ++numDef;
            }
            if (numDef > 1) ExtraChat.logger.warn("There is more than 1 default tag, this will cause problems.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalStateException e2) {
            ExtraChat.logger.info("Tags Json Empty");
        }

    }

    public static void removeTag(String UUID) {
        for (Tag t : Tag.tags) {
            if (t.getPlayers().containsKey(java.util.UUID.fromString(UUID))) {
                t.getPlayers().remove(java.util.UUID.fromString(UUID));
                break;
            }
        }
        updateTags();
    }

    public static void readBroadcasts() {
        try {
            JsonArray array = new JsonParser().parse(new JsonReader(new FileReader(broadcastjson))).getAsJsonArray();
            Iterator<JsonElement> it = array.iterator();
            while (it.hasNext()) {
                JsonObject obj = it.next().getAsJsonObject();
                TextBuilder builder = Texts.fromLegacy(obj.get("broadcast").getAsString(), '&').builder();
                JsonObject event = obj.getAsJsonObject("clickEvent");
                if (event.has("openURL")) {
                    builder.onClick(new ClickAction.OpenUrl(new URL(event.get("openURL").getAsString())));
                }
                if (event.has("suggestCommand")) {
                    builder.onClick(new ClickAction.SuggestCommand(event.get("suggestCommand").getAsString()));
                }
                if (event.has("runCommand")) {
                    builder.onClick(new ClickAction.RunCommand(event.get("runCommand").getAsString()));
                }
                if (obj.has("hoverText")) {
                    builder.onHover(new HoverAction.ShowText(Texts.fromLegacy(obj.get("hoverText").getAsString(), '&')));
                }
                Broadcaster.broadcasts.add(builder.build());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e2) {
            ExtraChat.logger.info("Broadcasts Json Empty");
        }
    }

    public static void readChatGroups() {
        try {
            JsonArray array = new JsonParser().parse(new JsonReader(new FileReader(chatGroups))).getAsJsonArray();
            Iterator<JsonElement> it = array.iterator();
            while (it.hasNext()) {
                JsonObject object = it.next().getAsJsonObject();
                ChatChannel.channels.add(new ChatChannel(object.get("name").getAsString(), object.get("tag").getAsString(),
                        object.get("radius").getAsInt(), object.get("default").getAsBoolean(), object.get("private").getAsBoolean(),
                        object.get("pass").getAsString()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void updateChatGroups() {
        JsonArray array = new JsonArray();
        for (ChatChannel chat : ChatChannel.channels) {
            JsonObject obj = new JsonObject();
            obj.addProperty("name", chat.getName());
            obj.addProperty("tag", chat.getTag());
            obj.addProperty("radius", chat.getRadius());
            obj.addProperty("default", chat.isDefault());
            obj.addProperty("private", chat.isPrivate());
            obj.addProperty("pass", chat.getPass());
            array.add(obj);
        }
        writeJson(chatGroups, array);
    }

    public static void updateTags() {
        JsonArray array = new JsonArray();
        for (Tag t : Tag.tags) {
            JsonObject obj = new JsonObject();
            obj.addProperty("name", t.getName());
            obj.addProperty("default", t.isDefault());
            if (!t.isDefault()) {
                JsonArray players = new JsonArray();
                for (UUID id : t.getPlayers().keySet()) {
                    JsonObject playerObj = new JsonObject();
                    playerObj.addProperty("playerName", t.getPlayers().get(id));
                    playerObj.addProperty("playerUUID", id.toString());
                    players.add(playerObj);
                }
                obj.add("players", players);
            }
            array.add(obj);
        }
        writeJson(tagsjson, array);
    }

    private static void writeJson(File f, JsonElement element) {
        try {
            FileOutputStream outputStream = new FileOutputStream(f);
            Gson gs = new GsonBuilder().setPrettyPrinting().create();
            outputStream.write(gs.toJson(element).getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void readBlacklist() {
        try {
            JsonArray array = new JsonParser().parse(new JsonReader(new FileReader(blacklistjson))).getAsJsonArray();
            Iterator<JsonElement> it = array.iterator();
            while (it.hasNext()) {
                JsonObject object = it.next().getAsJsonObject();
                String regex = object.get("regex").getAsString();
                JsonObject action = object.getAsJsonObject("action");
                //String privateMes = null;
                //if (object.has("privateMessage")) privateMes = object.get("privateMessage").getAsString();
                WordAction wordAction = WordAction.fromString(action.get("type").getAsString());
                if (wordAction == WordAction.KICK) {
                    String alert = null;
                    if (action.has("alert")) alert = action.get("alert").getAsString();
                    Boolean cancel = action.get("cancel").getAsBoolean();
                    String kick = action.get("kick").getAsString();
                    BlacklistedWord.blacklistedWordList.add(new BlacklistedWord(kick, wordAction, regex, cancel, alert));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalStateException e2) {
            ExtraChat.logger.info("Blacklist Json Empty");
        }
    }
}
