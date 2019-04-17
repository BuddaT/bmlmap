package net.bdew.wurm.bmlmap;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.mesh.Tiles.Tile;
import com.wurmonline.server.Server;
import com.wurmonline.server.zones.Zones;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class MapRender {
    private final static short interval = 200;

    private static final BufferedImage redX, mapBg;

    private static BufferedImage loadResImage(String name) throws IOException {
        return ImageIO.read(Objects.requireNonNull(MapRender.class.getClassLoader().getResource(name)));
    }

    static {
        try {
            redX = loadResImage("redx.png");
            mapBg = loadResImage("bg.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage createMapDump(int xo, int yo, int lWidth, int lHeight) {
        final BufferedImage render = new BufferedImage(lWidth, lHeight, BufferedImage.TYPE_INT_RGB);
        final float[] data = new float[lWidth * lHeight * 3];

        for (int x = 0; x < lWidth; x++) {
            for (int y = lHeight - 1; y >= 0; y--) {
                final short height = getSurfaceHeight(x + xo, y + yo);
                final short nearHeightNX = x == 0 ? height : getSurfaceHeight(x + xo - 1, y + yo);
                final short nearHeightNY = y == 0 ? height : getSurfaceHeight(x + xo, y + yo - 1);
                final short nearHeightX = x == lWidth - 1 ? height : getSurfaceHeight(x + xo + 1, y + yo);
                final short nearHeightY = y == lWidth - 1 ? height : getSurfaceHeight(x + xo, y + yo + 1);
                boolean isControur = checkContourLine(height, nearHeightNX)
                        || checkContourLine(height, nearHeightNY)
                        || checkContourLine(height, nearHeightX)
                        || checkContourLine(height, nearHeightY);

                final Tile tile = getTileType(x + xo, y + yo);
                final Color color;
                if (tile != null) {
                    color = tile.getColor();
                } else {
                    color = Tile.TILE_DIRT.getColor();
                }
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                if (isControur) {
                    r = 0;
                    g = 0;
                    b = 0;
                } else if (height < 0) {
                    r = (int) (r * 0.2f + 0.4f * 0.4f * 256f);
                    g = (int) (g * 0.2f + 0.5f * 0.4f * 256f);
                    b = (int) (b * 0.2f + 1.0f * 0.4f * 256f);
                }

                data[(x + y * lWidth) * 3 + 0] = r;
                data[(x + y * lWidth) * 3 + 1] = g;
                data[(x + y * lWidth) * 3 + 2] = b;
            }
        }

        render.getRaster().setPixels(0, 0, lWidth, lHeight, data);

        BufferedImage b = new BufferedImage(mapBg.getWidth(), mapBg.getHeight(), mapBg.getType());
        Graphics2D graphics = b.createGraphics();
        graphics.drawImage(mapBg, 0, 0, b.getWidth(), b.getHeight(), null);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        graphics.drawImage(render, 0, 0, b.getWidth(), b.getHeight(), null);
        graphics.drawImage(render, 1, 0, b.getWidth() + 1, b.getHeight(), null);
        graphics.drawImage(render, 0, 1, b.getWidth(), b.getHeight() + 1, null);
        graphics.drawImage(render, 1, 1, b.getWidth() + 1, b.getHeight() + 1, null);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        graphics.drawImage(redX, b.getWidth() / 2 - redX.getWidth() / 2, b.getHeight() / 2 - redX.getHeight() / 2, null);
        graphics.dispose();
        return b;
    }

    private static boolean checkContourLine(short h0, short h1) {
        if (h0 == h1) {
            return false;
        }
        for (int i = h0; i <= h1; i++) {
            if (i % interval == 0) {
                return true;
            }
        }
        return false;
    }

    private static short getSurfaceHeight(int x, int y) {
        if (x < 0) x = 0;
        if (x > Zones.worldTileSizeX) x = Zones.worldTileSizeX;
        if (y < 0) y = 0;
        if (y > Zones.worldTileSizeY) y = Zones.worldTileSizeY;
        return Tiles.decodeHeight(Server.surfaceMesh.getTile(x, y));
    }

    private static Tile getTileType(int x, int y) {
        if (x < 0) x = 0;
        if (x > Zones.worldTileSizeX) x = Zones.worldTileSizeX;
        if (y < 0) y = 0;
        if (y > Zones.worldTileSizeY) y = Zones.worldTileSizeY;
        return Tiles.getTile(Tiles.decodeType(Server.surfaceMesh.getTile(x, y)));
    }

}
