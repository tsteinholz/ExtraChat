package com.buuz135.api;

import org.mcstats.Metrics;
import org.spongepowered.api.Game;

import java.io.File;
import java.io.IOException;


public class ExtraMetrics extends Metrics {

    private Game game;

    public ExtraMetrics(String pluginName, String pluginVersion, Game game) throws IOException {
        super(pluginName, pluginVersion);
        this.game = game;
    }

    @Override
    public String getFullServerVersion() {
        return "1.8";
    }

    @Override
    public int getPlayersOnline() {
        return game.getServer().getOnlinePlayers().size();
    }

    @Override
    public File getConfigFile() {
        return new File("config/ExtraChat/metrics.yml");
    }
}
