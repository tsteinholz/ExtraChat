package com.buuz135.extrachat.broadcast;


import com.buuz135.extrachat.ExtraChat;
import com.buuz135.extrachat.config.ConfigLoader;
import org.spongepowered.api.service.scheduler.Task;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Broadcaster {
    public static List<Text> broadcasts = new ArrayList<Text>();
    public static Task task;

    public static void startBroadcastTask() {
        final Random rn = new Random();
        if (!ConfigLoader.broadcastEnabled) return;
        task = ExtraChat.game.getScheduler().getTaskBuilder().interval(ConfigLoader.broadcastTime).execute(new Runnable() {
            @Override
            public void run() {
                if (ExtraChat.game.getServer().getOnlinePlayers().size() == 0 || broadcasts.size() == 0) return;
                ExtraChat.game.getServer().getBroadcastSink().sendMessage(ConfigLoader.broadcastTag.builder()
                        .append(broadcasts.get(rn.nextInt(broadcasts.size()))).build());
            }
        }).submit(ExtraChat.pluginContainer);
    }
}
