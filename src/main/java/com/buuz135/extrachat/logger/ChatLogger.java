package com.buuz135.extrachat.logger;


import com.buuz135.extrachat.ExtraChat;
import com.buuz135.extrachat.config.ConfigLoader;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerChatEvent;
import org.spongepowered.api.text.Texts;

import java.io.*;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ChatLogger {

    public static PrintWriter writer;
    public static File fileTo;

    @Subscribe(order = Order.FIRST)
    public void playerChatLogger(PlayerChatEvent e) {
        if (!ConfigLoader.loggerEnabled) return;
        if (writer == null) {
            try {
                writer = createPrintWriter();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        Date d = new Date();
        String format = "[" + d.toLocaleString() + "]:";
        writer.println(format + Texts.toPlain(e.getMessage()));
        writer.flush();
        if (fileTo.length() > 100000) {
            try {
                if (ChatLogger.writer != null) ChatLogger.writer.close();
                if (ChatLogger.fileTo != null) {
                    ChatLogger.compressFile();
                    ChatLogger.fileTo.delete();
                }
                writer = createPrintWriter();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private PrintWriter createPrintWriter() throws IOException {
        File folder = new File(ConfigLoader.loggerPath);
        if (!folder.exists()) {
            folder.mkdir();
        }
        Date d = new Date();
        fileTo = new File(ConfigLoader.loggerPath + File.separator + d.toLocaleString().replaceAll(":", "-") + ".log");
        ExtraChat.logger.info(d.toLocaleString());
        fileTo.createNewFile();
        return new PrintWriter(fileTo);
    }

    public static void compressFile() {
        byte[] buffer = new byte[1024];
        try {
            FileOutputStream fos = new FileOutputStream(fileTo.getPath().replace(".log", ".zip"));
            ZipOutputStream zos = new ZipOutputStream(fos);
            ZipEntry ze = new ZipEntry(fileTo.getName());
            zos.putNextEntry(ze);
            FileInputStream in = new FileInputStream(fileTo);
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

}

