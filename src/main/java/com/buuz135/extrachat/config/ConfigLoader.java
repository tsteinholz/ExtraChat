package com.buuz135.extrachat.config;


import com.buuz135.extrachat.ExtraChat;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.TextMessageException;

import java.io.File;
import java.io.IOException;

public class ConfigLoader {
    //format="&a[&3%PLAYER%&a] &c&l>> "

    public static String formatMes;
    public static String formatTag;
    public static boolean loggerEnabled;
    public static String loggerPath;
    public static int broadcastTime;
    public static Text broadcastTag;
    public static boolean broadcastEnabled;
    public static String privateMessageFormat;
    public static boolean chatChannels;
    public static String channelFormat;
    public static boolean announce;

    public static void initConfiguration() {
        File folder = new File("config" + File.separator + "ExtraChat");
        if (!folder.exists()) folder.mkdir();
        File config = new File("config" + File.separator + "ExtraChat" + File.separator + "config.conf");
        if (!config.exists()) {
            ExtraChat.logger.warn("Configuration file not found, creating a new one.");
            try {
                config.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadConfig();
    }

    public static void loadConfig() {
        File config = new File("config" + File.separator + "ExtraChat" + File.separator + "config.conf");
        ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(config).build();
        try {
            CommentedConfigurationNode format = null;
            format = loader.load();
            formatMes = format.getNode("formatMes").getString();
            if (formatMes == null) {
                format.getNode("formatMes").setComment("Format of the chat message where %PLAYER% is the player and %MES% is the message.").setValue("<%PLAYER%> %MES%");
                formatMes = format.getNode("formatMes").getString();
            }
            formatTag = format.getNode("formatTag").getString();
            if (formatTag == null) {
                format.getNode("formatTag").setComment("Format of the tag where %TAG% is the tag.").setValue("%TAG% ");
                formatTag = format.getNode("formatTag").getString();
            }
            loggerEnabled = format.getNode("log").getNode("enabled").getBoolean();
            if (!format.getNode("log").getNode("enabled").getComment().isPresent()) {
                format.getNode("log").getNode("enabled").setComment("Set to true to enable the chat logger, default true.").setValue(true);
                loggerEnabled = format.getNode("log").getNode("enabled").getBoolean();
            }
            loggerPath = format.getNode("log").getNode("destination").getString();
            if (loggerPath == null) {
                format.getNode("log").getNode("destination").setComment("Define the path of the log, default chatlog").setValue("chatlog");
                loggerPath = format.getNode("log").getNode("destination").getString();
            }
            broadcastTime = format.getNode("broadcaster").getNode("time").getInt();
            if (broadcastTime == 0) {
                format.getNode("broadcaster").getNode("time").setComment("Time in ticks in which the broadcaster will transmit a message. (20 ticks = 1 second)").setValue(1200);
                broadcastTime = format.getNode("broadcaster").getNode("time").getInt();
            }
            broadcastEnabled = format.getNode("broadcaster").getNode("enabled").getBoolean();
            if (!format.getNode("broadcaster").getNode("enabled").getComment().isPresent()) {
                format.getNode("broadcaster").getNode("enabled").setComment("Set to true to enable the broadcaster.").setValue("true");
                broadcastEnabled = format.getNode("broadcaster").getNode("enabled").getBoolean();
            }
            String formatBR = format.getNode("broadcaster").getNode("format").getString();
            if (formatBR == null) {
                format.getNode("broadcaster").getNode("format").setComment("Format of the tag that will show in front of the broadcast.").setValue(" &7[&4!&7] ");
            }
            try {
                broadcastTag = Texts.legacy().from(format.getNode("broadcaster").getNode("format").getString().replaceAll("&", "" + Texts.getLegacyChar()));
            } catch (TextMessageException e) {
                e.printStackTrace();
            }
            privateMessageFormat = format.getNode("privateMessage").getNode("format").getString();
            if (privateMessageFormat == null) {
                format.getNode("privateMessage").getNode("format").setComment("Format of the private message where %SENDER%" +
                        " is the player who send the message, %RECI% is the player who gets it, and %MES% is the actual message.");
                format.getNode("privateMessage").getNode("format").setValue("&7[&e%SENDER% &6-> &e%RECI%&7]&f: %MES%");
                privateMessageFormat = format.getNode("privateMessage").getNode("format").getString();
            }
            if (!format.getNode("chatChannel").getNode("enabled").getComment().isPresent()) {
                format.getNode("chatChannel").getNode("enabled").setValue(true);
                format.getNode("chatChannel").getNode("enabled").setComment("Set to true to enable the chat groups");
                format.getNode("chatChannel").getNode("chatTagFormat").setValue("[%TAG%]");
                format.getNode("chatChannel").getNode("chatTagFormat").setComment("The format of the tag where %TAG% is the channel tag.");
                format.getNode("chatChannel").getNode("announceChannelJoinOnPlayerJoin").setValue(false);
                format.getNode("chatChannel").getNode("announceChannelJoinOnPlayerJoin").setComment("Set to true to announce when a player joins a default channel when he joins the game.");
            }
            chatChannels = format.getNode("chatChannel").getNode("enabled").getBoolean();
            announce = format.getNode("chatChannel").getNode("announceChannelJoinOnPlayerJoin").getBoolean();
            channelFormat = format.getNode("chatChannel").getNode("chatTagFormat").getString();
            loader.save(format);
        } catch (IOException e) {
            ExtraChat.logger.error("Unable to load the configuration file.");
        }
    }

    public static void saveConfig(String formated, String node) {
        File file = new File("config" + File.separator + "ExtraChat" + File.separator + "config.conf");
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

    public static void toggleLog() {
        loggerEnabled = !loggerEnabled;
        File file = new File("config" + File.separator + "ExtraChat" + File.separator + "config.conf");
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

}
