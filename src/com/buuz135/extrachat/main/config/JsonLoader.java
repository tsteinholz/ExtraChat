package com.buuz135.extrachat.main.config;

import com.buuz135.extrachat.main.ExtraChat;
import com.buuz135.extrachat.main.Tag;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class JsonLoader {

    public static void initTagJson() {
        File json = new File("config/ExtraChat/tags.json");
        if (!json.exists()) {
            try {
                json.createNewFile();
            } catch (IOException e) {
                ExtraChat.logger.error("Error creating the tags file.");
            }
        }
        readJson();
//        insertTag("tagTest1", "name1","38553ec3-7569-4811-97d8-5743c980bbf9");
//        insertTag("tagTest1", "name2", UUID.randomUUID().toString());
//        insertTag("tagTest1", "name3", UUID.randomUUID().toString());
//        insertTag("tagTest2", "name1", UUID.randomUUID().toString());

    }

    public static void insertTag(String tag, String name, String UUID) {
        JsonWriter writer = null;
        for (Tag t : Tag.tags) {
            if (t.getPlayers().containsKey(java.util.UUID.fromString(UUID))) {
                t.getPlayers().remove(java.util.UUID.fromString(UUID));
                break;
            }
        }
        Tag.getTagByName(tag).getPlayers().put(java.util.UUID.fromString(UUID),name);
        try {
            writer = new JsonWriter(new FileWriter("config/ExtraChat/tags.json"));
            writer.setIndent("    ");
            writer.beginArray();
            for (Tag temp : Tag.tags){
                writer.beginObject();
                writer.name(temp.getName());
                writer.beginArray();
                for (UUID uuid : temp.getPlayers().keySet()){
                    writer.beginObject();
                    writer.name(temp.getPlayers().get(uuid));
                    writer.value(uuid.toString());
                    writer.endObject();
                }
                writer.endArray();
                writer.endObject();
            }
            writer.endArray();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readJson() {
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
                        tag.getPlayers().put(UUID.fromString(reader.nextString()),name);
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
}
