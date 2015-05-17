package com.buuz135.extrachat.logger;


import com.buuz135.extrachat.config.ConfigLoader;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerChatEvent;
import org.spongepowered.api.event.entity.player.PlayerDeathEvent;
import org.spongepowered.api.event.entity.player.PlayerJoinEvent;
import org.spongepowered.api.event.entity.player.PlayerQuitEvent;
import org.spongepowered.api.event.message.CommandEvent;
import org.spongepowered.api.text.Texts;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ChatLogger {

    public static PrintWriter writer;
    public static File fileTo;

    @Subscribe(order = Order.LAST)
    public void playerChatLogger(PlayerChatEvent e) {
        insertLine(Texts.toPlain(e.getMessage()));
    }

    @Subscribe(order = Order.LAST)
    public void playerJoin(PlayerJoinEvent event){
        if (event.getJoinMessage() != null)
        insertLine("[JOIN] "+Texts.toPlain(event.getJoinMessage()));
    }

    @Subscribe(order = Order.LAST)
    public void playerQuit(PlayerQuitEvent event){
        if (event.getQuitMessage() != null)
        insertLine("[QUIT] "+Texts.toPlain(event.getQuitMessage()));
    }

    @Subscribe(order = Order.LAST)
    public void playerCommand(CommandEvent event){
        insertLine("[CMD] Player "+event.getSource().getName()+" executed the command /"+event.getCommand()+" "+ event.getArguments());
    }

    @Subscribe(order = Order.LAST)
    public void playerDeath(PlayerDeathEvent event){
        insertLine("[DEATH] "+Texts.toPlain(event.getDeathMessage()));
    }

    private static PrintWriter createPrintWriter() throws IOException {
        File folder = new File(ConfigLoader.loggerPath);
        if (!folder.exists()) {
            folder.mkdir();
        }
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-M-d H-m");
        fileTo = new File(ConfigLoader.loggerPath + File.separator + sdf.format(d) + ".log");
        fileTo.createNewFile();
        return new PrintWriter(fileTo);
    }

    public static void compressFile(File f) {
        byte[] buffer = new byte[1024];
        try {
            FileOutputStream fos = new FileOutputStream(f.getPath().replace(".log", ".zip"));
            ZipOutputStream zos = new ZipOutputStream(fos);
            ZipEntry ze = new ZipEntry(f.getName());
            zos.putNextEntry(ze);
            FileInputStream in = new FileInputStream(f);
            int len;
            while ((len = in.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            in.close();
            zos.closeEntry();
            zos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static void insertLine(String line){
        if (!ConfigLoader.loggerEnabled) return;
        if (writer == null) {
            try {
                writer = createPrintWriter();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("d-M-YYY H:m:s");
        String format = "[" + sdf.format(d) + "]: ";
        writer.println(format + line);
        writer.flush();
        if (fileTo.length() > 100000) {
            try {
                if (ChatLogger.writer != null) ChatLogger.writer.close();
                if (ChatLogger.fileTo != null) {
                    ChatLogger.compressFile(fileTo);
                    ChatLogger.fileTo.delete();
                }
                writer = createPrintWriter();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

}

