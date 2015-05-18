package com.buuz135.extrachat.config;


import com.buuz135.extrachat.ChatChannel;
import com.buuz135.extrachat.ExtraChat;
import com.buuz135.extrachat.Tag;
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

    public static void initTagJson() {
        File tagsjson = new File("config/ExtraChat/tags.json");
        File broadcastjson = new File("config/ExtraChat/broadcasts.json");
        File chatGroups = new File("config"+File.separator+"ExtraChat"+File.separator+"chatGroups.json");
        if (!tagsjson.exists()) {
            try {
                tagsjson.createNewFile();
            } catch (IOException e) {
                ExtraChat.logger.error("Error creating the tags file.");
            }
        }
        readTagsJson();
        if (!broadcastjson.exists()) {
            try {
                broadcastjson.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!chatGroups.exists()){
            try {
                chatGroups.createNewFile();
                ChatChannel.channels.add(new ChatChannel("global","G",-1,true,false,""));
                ChatChannel.channels.add(new ChatChannel("local","L",200,true,false,""));
                updateChatGroups();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        readChatGroups();
        readBroadcasts();
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
        try {
            writer = new JsonWriter(new FileWriter("config/ExtraChat/tags.json"));
            writer.setIndent("    ");
            writer.beginArray();
            for (Tag temp : Tag.tags) {
                if (!temp.getPlayers().isEmpty()) {
                    writer.beginObject();
                    writer.name(temp.getName());
                    writer.beginArray();
                    for (UUID uuid : temp.getPlayers().keySet()) {
                        writer.beginObject();
                        writer.name(temp.getPlayers().get(uuid));
                        writer.value(uuid.toString());
                        writer.endObject();
                    }
                    writer.endArray();
                    writer.endObject();
                }
            }
            writer.endArray();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readTagsJson() {
        try {
            JsonReader reader = new JsonReader(new FileReader("config/ExtraChat/tags.json"));
            if (!reader.hasNext()) return;
            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();
                while (reader.hasNext()) {
                    Tag tag = new Tag(reader.nextName());
                    reader.beginArray();
                    while (reader.hasNext()) {
                        reader.beginObject();
                        String name = reader.nextName();
                        tag.getPlayers().put(UUID.fromString(reader.nextString()), name);
                        reader.endObject();
                    }
                    reader.endArray();
                    Tag.tags.add(tag);
                }
                reader.endObject();
            }
            reader.endArray();
            reader.close();
        } catch (IOException e) {
            ExtraChat.logger.warn("Tag json empty");
        }
    }

    public static void removeTag(String UUID) {
        JsonWriter writer = null;
        for (Tag t : Tag.tags) {
            if (t.getPlayers().containsKey(java.util.UUID.fromString(UUID))) {
                t.getPlayers().remove(java.util.UUID.fromString(UUID));
                break;
            }
        }
        try {
            writer = new JsonWriter(new FileWriter("config/ExtraChat/tags.json"));
            writer.setIndent("    ");
            writer.beginArray();
            for (Tag temp : Tag.tags) {
                if (!temp.getPlayers().isEmpty()) {
                    writer.beginObject();
                    writer.name(temp.getName());
                    writer.beginArray();
                    for (UUID uuid : temp.getPlayers().keySet()) {
                        writer.beginObject();
                        writer.name(temp.getPlayers().get(uuid));
                        writer.value(uuid.toString());
                        writer.endObject();
                    }
                    writer.endArray();
                    writer.endObject();
                }
            }
            writer.endArray();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readBroadcasts() {
        try {
            JsonReader reader = new JsonReader(new FileReader("config/ExtraChat/broadcasts.json"));
            reader.setLenient(true);
            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();
                TextBuilder builder = Texts.fromLegacy(reader.nextName(), '&').builder();
                String onClick = "";
                String hover = "";
                reader.beginArray();
                reader.beginObject();
                reader.nextName();
                reader.beginObject();
                onClick = reader.nextName();
                if (!onClick.equals("null")) {
                    if (onClick.equals("openURL")) {
                        builder.onClick(new ClickAction.OpenUrl(new URL(reader.nextString())));
                    }
                    if (onClick.equals("suggestCommand")) {
                        builder.onClick(new ClickAction.SuggestCommand(reader.nextString()));
                    }
                    if (onClick.equals("runCommand")) {
                        builder.onClick(new ClickAction.RunCommand(reader.nextString()));
                    }
                } else {
                    reader.nextString();
                }
                reader.endObject();
                reader.endObject();
                reader.beginObject();
                reader.nextName();
                hover = reader.nextString();
                if (!hover.equals("null")) {
                    builder.onHover(new HoverAction.ShowText(Texts.fromLegacy(hover)));
                }
                reader.endObject();
                reader.endArray();
                reader.endObject();
                Broadcaster.broadcasts.add(builder.build());
            }
            reader.endArray();
            reader.close();
        } catch (IOException e) {
            ExtraChat.logger.warn("Broadcast json empty");
        }
    }

    public static void readChatGroups(){
        try {
            JsonArray array = new JsonParser().parse(new JsonReader(new FileReader("config"+File.separator+"ExtraChat"+File.separator
            +"chatGroups.json"))).getAsJsonArray();
            Iterator<JsonElement> it = array.iterator();
            while (it.hasNext()){
                JsonObject object = it.next().getAsJsonObject();
                ChatChannel.channels.add(new ChatChannel(object.get("name").getAsString(), object.get("tag").getAsString(),
                        object.get("radius").getAsInt(), object.get("default").getAsBoolean(), object.get("private").getAsBoolean(),
                        object.get("pass").getAsString()));
            }
            ExtraChat.logger.info("Total channels: "+ChatChannel.channels.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void updateChatGroups(){
        JsonArray array = new JsonArray();
        for (ChatChannel chat : ChatChannel.channels){
            JsonObject obj = new JsonObject();
            obj.addProperty("name", chat.getName());
            obj.addProperty("tag",chat.getPass());
            obj.addProperty("radius",chat.getRadius());
            obj.addProperty("default",chat.isDefault());
            obj.addProperty("private",chat.isPrivate());
            obj.addProperty("pass",chat.getPass());
            array.add(obj);
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(new File("config"+File.separator+"ExtraChat"+File.separator+"chatGroups.json"));
            Gson gs = new GsonBuilder().setPrettyPrinting().create();
            outputStream.write(gs.toJson(array).getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
