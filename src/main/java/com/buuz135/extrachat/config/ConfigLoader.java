package com.buuz135.extrachat.config;


import com.buuz135.extrachat.ExtraChat;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigLoader {
    //format="&a[&3%PLAYER%&a] &c&l>> "

    public static String formatMes;
    public static String formatTag;
    public static double version;
    public static List<String> blacklisted;
    public static int style;
    public static boolean loggerEnabled;
    public static String loggerPath;
    public static int replaceInt;
    public static boolean replaceEnabled;
    public static int broadcastTime;
    public static Text broadcastTag;
    public static boolean broadcastEnabled;

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
            ExtraChat.logger.info("Config version " + version);
            if (version < 1.2) {
                format.getNode("version").setValue("1.2");
                version = 1.2;
                format.getNode("blacklist").getNode("style").setComment("Define the blacklist style: 1. '****', 2.  '@#%&'").setValue("1");
                format.getNode("blacklist").getNode("words").setComment("Blacklisted words in this format 'word,word' without ''").setValue("lag");
                format.getNode("log").getNode("enabled").setComment("Set to true to enable the chat logger, default true.").setValue(true);
                format.getNode("log").getNode("destination").setComment("Define the path of the log, default chatlog").setValue("chatlog");
                format.getNode("wordReplacer").getNode("size").setComment("The amount of chat messages back you can fix.").setValue(10);
                format.getNode("wordReplacer").getNode("enabled").setComment("Set to true to enable the word replacer.").setValue(true);
                loader.save(format);
            }
            if (version < 1.3) {
                format.getNode("version").setValue("1.3");
                version = 1.3;
                format.getNode("broadcaster").getNode("enabled").setComment("Set to true to enable the broadcaster.").setValue("true");
                format.getNode("broadcaster").getNode("time").setComment("Time in ticks in which the broadcaster will transmit a message. (20 ticks = 1 second)").setValue(1200);
                format.getNode("broadcaster").getNode("format").setComment("Format of the tag that will show in front of the broadcast.").setValue(" &7[&4!&7] ");
                loader.save(format);
            }
            formatMes = format.getNode("formatMes").getValue().toString();
            formatTag = format.getNode("formatTag").getValue().toString();
            blacklisted = new ArrayList<String>();
            for (String s : format.getNode("blacklist").getNode("words").getString().split(",")) {
                blacklisted.add(s);
            }
            style = format.getNode("blacklist").getNode("style").getInt();
            loggerEnabled = format.getNode("log").getNode("enabled").getBoolean();
            loggerPath = format.getNode("log").getNode("destination").getString();
            replaceEnabled = format.getNode("wordReplacer").getNode("enabled").getBoolean();
            replaceInt = format.getNode("wordReplacer").getNode("size").getInt();
            broadcastTime = format.getNode("broadcaster").getNode("time").getInt();
            broadcastEnabled = format.getNode("broadcaster").getNode("enabled").getBoolean();
            broadcastTag = Texts.fromLegacy(format.getNode("broadcaster").getNode("format").getString(), '&');
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
            format.getNode("version").setComment("Config version").setValue("1.2");
            format.getNode("formatMes").setComment("Format of the chat message where %PLAYER% is the player and %MES% is the message.").setValue("<%PLAYER%> %MES%");
            format.getNode("formatTag").setComment("Format of the tag where %TAG% is the tag.").setValue("%TAG% ");
            format.getNode("blacklist").getNode("style").setComment("Define the blacklist style: 1. '****', 2.  '@#%&'").setValue("1");
            format.getNode("blacklist").getNode("words").setComment("Blacklisted words in this format 'word,word' without ''").setValue("lag");
            format.getNode("log").getNode("enabled").setComment("Set to true to enable the chat logger, default true.").setValue(true);
            format.getNode("log").getNode("destination").setComment("Define the path of the log, default chatlog").setValue("chatlog");
            format.getNode("wordReplacer").getNode("size").setComment("The amount of chat messages back you can fix.").setValue(10);
            format.getNode("wordReplacer").getNode("enabled").setComment("Set to true to enable the word replacer.").setValue(true);
            format.getNode("broadcaster").getNode("enabled").setComment("Set to true to enable the broadcaster.").setValue("true");
            format.getNode("broadcaster").getNode("time").setComment("Time in ticks in which the broadcaster will transmit a message. (20 ticks = 1 second)").setValue(1200);
            format.getNode("broadcaster").getNode("format").setComment("Format of the tag that will show in front of the broadcast.").setValue(" &7[&4!&7] ");
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

    public static void toggleLog() {
        loggerEnabled = !loggerEnabled;
        File file = new File("config/ExtraChat/config.conf");
        ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(file).build();
        CommentedConfigurationNode format = null;
        try {
            loader.createEmptyNode(ConfigurationOptions.defaults());
            format = loader.load();
            format.getNode("log").getNode("enabled").setValue(loggerEnabled);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void toggleReplace() {
        replaceEnabled = !replaceEnabled;
        File file = new File("config/ExtraChat/config.conf");
        ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(file).build();
        CommentedConfigurationNode format = null;
        try {
            loader.createEmptyNode(ConfigurationOptions.defaults());
            format = loader.load();
            format.getNode("wordReplacer").getNode("enabled").setValue(replaceEnabled);
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
            for (String s : blacklisted) {
                if (s != null && !s.equals(word)) {
                    temp = temp + s + ",";
                }
            }
            format.getNode("blacklist").setValue(temp);
            if (blacklisted.contains(word)) {
                blacklisted.remove(word);
            }
            loader.save(format);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
