package com.buuz135.extrachat.main.config;


import com.buuz135.extrachat.main.ExtraChat;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigLoader {
    //format="&a[&3%PLAYER%&a] &c&l>> "

    public static String formatMes;
    public static String formatTag;
    public static double version;
    public static List<String> blacklisted;
    public static int style;

    public static void initConfiguration() {
        File config = new File("config/ExtraChat/config.conf");
        if (!config.exists()) {
            ExtraChat.logger.warn("Configuration file not found, creating a new one.");
            createConfiguration(config);
        }
        loadConfig();
    }

    public static void loadConfig() {
        File config = new File("config/ExtraChat/config.conf");
        ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(config).build();
        try {
            CommentedConfigurationNode format = null;
            format = loader.load();
            version = format.getNode("version").getDouble();
            if (version != 1.2) {
                format.getNode("version").setValue("1.2");
                version = 1.2;
                format.getNode("blacklistStyle").setComment("Define the blacklist style: 1. '****', 2.  '@#%&'");
                format.getNode("blacklistStyle").setValue("1");
                format.getNode("blacklist").setComment("Blacklisted words in this format 'word,word' without ''");
                format.getNode("blacklist").setValue("lag,");
                loader.save(format);
            }
            formatMes = format.getNode("formatMes").getValue().toString();
            formatTag = format.getNode("formatTag").getValue().toString();
            blacklisted = new ArrayList<String>();
            for (String s : format.getNode("blacklist").getString().split(",")){
                blacklisted.add(s);
            }
            style = format.getNode("blacklistStyle").getInt();
            ExtraChat.logger.info("Config version " + version);
        } catch (IOException e) {
            ExtraChat.logger.error("Unable to load the configuration file.");
        }
    }

    public static void saveConfig(String formated, String node) {
        File file = new File("config/ExtraChat/config.conf");
        ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(file).build();
        CommentedConfigurationNode format = null;
        try {
            loader.createEmptyNode(ConfigurationOptions.defaults());
            format = loader.load();
            format.getNode(node).setValue(formated);
            loader.save(format);
            if (node.equals("formatMes")) {
                formatMes = formated;
            } else {
                formatTag = formated;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void createConfiguration(File file) {
        try {
            new File("config/ExtraChat").mkdir();
            file.createNewFile();
        } catch (IOException e) {
            ExtraChat.logger.error("Unable to create configuration file.");
        }
        ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(file).build();
        CommentedConfigurationNode format = null;
        try {
            loader.createEmptyNode(ConfigurationOptions.defaults());
            format = loader.load();
            format.getNode("version").setComment("Config version");
            format.getNode("version").setValue("1.2");
            format.getNode("formatMes").setComment("Format of the chat message where %PLAYER% is the player and %MES% is the message.");
            format.getNode("formatMes").setValue("<%PLAYER%> %MES%");
            format.getNode("formatTag").setComment("Format of the tag where %TAG% is the tag.");
            format.getNode("formatTag").setValue("%TAG% ");
            format.getNode("blacklist").setComment("Blacklisted words in this format 'word,word' without ''");
            format.getNode("blacklist").setValue("lag,");
            format.getNode("blacklistStyle").setComment("Define the blacklist style: 1. '****', 2.  '@#%&'");
            format.getNode("blacklistStyle").setValue("1");
            loader.save(format);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void addWordtoBlackList(String word) {
        ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(new File("config/ExtraChat/config.conf")).build();
        CommentedConfigurationNode format = null;
        try {
            loader.createEmptyNode(ConfigurationOptions.defaults());
            format = loader.load();
            format.getNode("blacklist").setValue(format.getNode("blacklist").getValue().toString() + word + ",");
            blacklisted.add(word);
            loader.save(format);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeWordFromBlackList(String word) {
        ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(new File("config/ExtraChat/config.conf")).build();
        CommentedConfigurationNode format = null;
        try {
            loader.createEmptyNode(ConfigurationOptions.defaults());
            format = loader.load();
            ExtraChat.logger.info(word);
            String temp = "";
            for (String s : blacklisted){
                if(s!=null && !s.equals(word)){
                    temp = temp + s + ",";
                }
            }
            format.getNode("blacklist").setValue(temp);
            if(blacklisted.contains(word)){
                blacklisted.remove(word);
            }
            loader.save(format);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
