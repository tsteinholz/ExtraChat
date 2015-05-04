package com.buuz135.extrachat;


import com.buuz135.extrachat.broadcast.BRCommand;
import com.buuz135.extrachat.broadcast.Broadcaster;
import com.buuz135.extrachat.commands.ExtraChatCommand;
import com.buuz135.extrachat.config.ConfigLoader;
import com.buuz135.extrachat.config.JsonLoader;
import com.buuz135.extrachat.events.PlayerChat;
import com.buuz135.extrachat.logger.ChatLogger;
import com.buuz135.extrachat.logger.ReplaceLogger;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.InitializationEvent;
import org.spongepowered.api.event.state.PostInitializationEvent;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;


@Plugin(id = "ExtraChat", name = "ExtraChat", version = "1.0")
public class ExtraChat {
    public static Logger logger;
    public static PluginContainer pluginContainer;
    public static Game game;
    public static ReplaceLogger replaceLogger;


    @Subscribe
    public void preInit(PreInitializationEvent event) {
        pluginContainer = event.getGame().getPluginManager().getPlugin("ExtraChat").get();
        logger = event.getGame().getPluginManager().getLogger(pluginContainer);
        game = event.getGame();
        event.getGame().getCommandDispatcher().register(pluginContainer.getInstance(), new ExtraChatCommand(), "ec");
        event.getGame().getCommandDispatcher().register(pluginContainer.getInstance(), new BRCommand(), "br");
    }

    @Subscribe
    public void init(InitializationEvent event) {
        ConfigLoader.initConfiguration();
        JsonLoader.initTagJson();
        event.getGame().getEventManager().register(pluginContainer.getInstance(), new PlayerChat());
        event.getGame().getEventManager().register(pluginContainer.getInstance(), new ChatLogger());
    }

    @Subscribe
    public void postInit(PostInitializationEvent event) {
        replaceLogger = new ReplaceLogger();
        Broadcaster.startBroadcastTask();
    }

    @Subscribe
    public void serverClose(ServerStoppingEvent event) {
        if (ChatLogger.writer != null) ChatLogger.writer.close();
        if (ChatLogger.fileTo != null) {
            ChatLogger.compressFile();
            ChatLogger.fileTo.delete();
        }
    }
}
