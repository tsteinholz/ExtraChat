package com.buuz135.extrachat.main.config;


import com.buuz135.extrachat.main.ExtraChat;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;

public class ConfigLoader {
    //format="&a[&3%PLAYER%&a] &c&l>> "

    public static String formatMes;
    public static String formatTag;
    public static double version;

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
            formatMes = format.getNode("formatMessage").getValue().toString();
            formatTag = format.getNode("formatTag").getValue().toString();
            if (version == 0.0){
                format.getNode("version").setComment("Config version");
                format.getNode("version").setValue("1.1");
                version = 1.1;
                format.getNode("formatMessage").setComment("Format of the chat message where %PLAYER% is the player and %MES% is the message.");
                format.getNode("formatMessage").setValue(formatMes);
                format.getNode("formatTag").setComment("Format of the tag where %TAG% is the tag.");
                format.getNode("formatTag").setValue("%TAG%");
                loader.save(format);
            }
            ExtraChat.logger.info("Config version "+ version);
        } catch (IOException e) {
            ExtraChat.logger.error("Unable to load the configuration file.");
        }
    }

    public static void saveConfig(String formated) {
        File file = new File("config/ExtraChat/config.conf");
        ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(file).build();
        CommentedConfigurationNode format = null;
        try {
            loader.createEmptyNode(ConfigurationOptions.defaults());
            format = loader.load();
            format.getNode("format").setValue(formated);
            loader.save(format);
            formatMes = formated;
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
            format.getNode("version").setValue("1.1");
            format.getNode("formatMessage").setComment("Format of the chat message where %PLAYER% is the player and %MES% is the message.");
            format.getNode("formatMessage").setValue("<%PLAYER%> %MES%");
            format.getNode("formatTag").setComment("Format of the tag where %TAG% is the tag.");
            format.getNode("formatTag").setValue("%TAG% ");
            loader.save(format);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
