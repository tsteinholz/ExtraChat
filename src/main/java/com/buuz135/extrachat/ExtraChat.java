package com.buuz135.extrachat;


import com.buuz135.api.ExtraMetrics;
import com.buuz135.extrachat.broadcast.Broadcaster;
import com.buuz135.extrachat.commands.CommandRegister;
import com.buuz135.extrachat.config.ConfigLoader;
import com.buuz135.extrachat.config.JsonLoader;
import com.buuz135.extrachat.events.PlayerChat;
import com.buuz135.extrachat.events.PlayerJoin;
import com.buuz135.extrachat.logger.ChatLogger;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.*;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.io.File;
import java.io.IOException;


@Plugin(id = "EC", name = "ExtraChat", version = "1.4")
public class ExtraChat {

    @Inject public static Logger logger;
    @Inject public static PluginContainer pluginContainer;
    @Inject public static Game game;

    @Subscribe
    public void init(InitializationEvent event) {
        ConfigLoader.initConfiguration();
        if (new File(ConfigLoader.loggerPath).exists()) {
            for (File f : new File(ConfigLoader.loggerPath).listFiles()) {
                if (f.getName().contains(".log")) {
                    ChatLogger.compressFile(f);
                    f.delete();
                }
            }
        }
        JsonLoader.initJson();
        event.getGame().getEventManager().register(pluginContainer.getInstance(), new PlayerChat());
        event.getGame().getEventManager().register(pluginContainer.getInstance(), new ChatLogger());
        event.getGame().getEventManager().register(pluginContainer.getInstance(), new PlayerJoin());
    }

    @Subscribe
    public void postInit(final PostInitializationEvent event) {
        Broadcaster.startBroadcastTask();
    }

    @Subscribe
    public void serverClose(ServerStoppingEvent event) {
        if (ChatLogger.writer != null) ChatLogger.writer.close();
        if (ChatLogger.fileTo != null) {
            ChatLogger.compressFile(ChatLogger.fileTo);
            ChatLogger.fileTo.delete();
        }
    }

    @Subscribe
    public void serverStart(ServerStartedEvent event) {
        try {
            ExtraMetrics metrics = new ExtraMetrics("ExtraChat", "1.4", event.getGame());
            metrics.start();
            if (!metrics.isOptOut()) {
                logger.info("Metrics module enabled.");
            } else {
                logger.info("Metrics module disabled.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
