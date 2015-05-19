package com.buuz135.extrachat;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatChannel {

    public static List<ChatChannel> channels = new ArrayList<ChatChannel>();


    private String name;
    private String tag;
    private int radius;
    private boolean isDefault;
    private boolean isPrivate;
    private String pass;
    private List<UUID> players;

    public ChatChannel(String name, String tag, int radius, boolean isPrivate, boolean isDefault, String pass) {
        this.name = name;
        this.tag = tag;
        this.radius = radius;
        this.isPrivate = isPrivate;
        this.isDefault = isDefault;
        this.pass = pass;
        players = new ArrayList<UUID>();
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
