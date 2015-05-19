package com.buuz135.extrachat;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class Tag {
    public static List<Tag> tags = new ArrayList<Tag>();

    public static Tag getTagByName(String tag) {
        for (Tag t : tags) {
            if (t.getName().equals(tag)) {
                return t;
            }
        }
        Tag temp = new Tag(tag, false);
        tags.add(temp);
        return temp;
    }

    private String name;
    private LinkedHashMap<UUID, String> players;
    private boolean isDefault;

    public Tag(String name, boolean isDefault) {
        this.name = name;
        this.isDefault = isDefault;
        players = new LinkedHashMap<UUID, String>();
    }

    public LinkedHashMap<UUID, String> getPlayers() {
        return players;
    }

    public String getName() {
        return name;
    }

    public boolean isDefault() {
        return isDefault;
    }
}
