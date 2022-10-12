package rasterize;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RasterBufferImage implements Raster {

    private final BufferedImage img;

    public RasterBufferImage(int width, int height) {
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public void setPixel(int x, int y, Color color) {
        try {
            img.setRGB(x, y, color.getRGB());
        } catch (Exception ignored) {
        }
    }

    @Override
    public int getPixel(int x, int y) {
        return img.getRGB(x, y);
    }

    public void present(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }

    public void clear() {
        Graphics gr = img.getGraphics();
        gr.setColor(new Color(0x1a1a1a));
        gr.fillRect(0, 0, img.getWidth(), img.getHeight());
    }

    public BufferedImage getImg() {
        return img;
    }
}
