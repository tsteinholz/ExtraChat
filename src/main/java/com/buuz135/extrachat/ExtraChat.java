package com.buuz135.extrachat;


import com.buuz135.api.ExtraMetrics;
import com.buuz135.extrachat.broadcast.BRCommand;
import com.buuz135.extrachat.broadcast.Broadcaster;
import com.buuz135.extrachat.commands.ExtraChatCommand;
import com.buuz135.extrachat.commands.ShowCommand;
import com.buuz135.extrachat.config.ConfigLoader;
import com.buuz135.extrachat.config.JsonLoader;
import com.buuz135.extrachat.events.PlayerChat;
import com.buuz135.extrachat.logger.ChatLogger;
import com.buuz135.extrachat.logger.ReplaceLogger;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.*;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.io.File;
import java.io.IOException;


@Plugin(id = "EC", name = "ExtraChat", version = "1.3")
public class ExtraChat {
    public static Logger logger;
    public static PluginContainer pluginContainer;
    public static Game game;
    public static ReplaceLogger replaceLogger;


    @Subscribe
    public void preInit(PreInitializationEvent event) {
        pluginContainer = event.getGame().getPluginManager().getPlugin("EC").get();
        logger = event.getGame().getPluginManager().getLogger(pluginContainer);
        game = event.getGame();
        event.getGame().getCommandDispatcher().register(pluginContainer.getInstance(), new ExtraChatCommand(), "ec", "extrachat");
        event.getGame().getCommandDispatcher().register(pluginContainer.getInstance(), new BRCommand(), "br", "broadcast");
        event.getGame().getCommandDispatcher().register(pluginContainer.getInstance(), new ShowCommand(), "show");
    }

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
        JsonLoader.initTagJson();
        event.getGame().getEventManager().register(pluginContainer.getInstance(), new PlayerChat());
        event.getGame().getEventManager().register(pluginContainer.getInstance(), new ChatLogger());
    }

    @Subscribe
    public void postInit(final PostInitializationEvent event) {
        replaceLogger = new ReplaceLogger();
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
            ExtraMetrics metrics = new ExtraMetrics("ExtraChat", "1.3", event.getGame());
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
