package net.bdew.wurm.bmlmap;

import org.gotti.wurmunlimited.mods.httpserver.api.ModHttpServer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class HttpHandler {
    private static Logger log = Logger.getLogger("BmlMapHttp");

    public static URI addr;

    public static InputStream handle(String url) {
        log.info(String.format("Req for :%s", url));
        BufferedImage img = new BufferedImage(512, 512, BufferedImage.TYPE_3BYTE_BGR);
        Graphics gfx = img.getGraphics();
        gfx.setColor(Color.WHITE);
        gfx.fillRect(0, 0, 512, 512);
        gfx.setColor(Color.BLACK);
        gfx.drawString("Hi i was drawn on the server!", 10, 10);
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ImageIO.write(img, "png", bos);
            return new ByteArrayInputStream(bos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void start(BmlMapMod mod) {
        String prefix = ModHttpServer.getInstance().serve(mod, Pattern.compile("^/(?<path>[^/]*)$"), HttpHandler::handle);
        try {
            if (prefix != null) {
                addr = ModHttpServer.getInstance().getUri().resolve(prefix);
                log.info(String.format("Started at %s", addr));
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
