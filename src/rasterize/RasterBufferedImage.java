package rasterize;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RasterBufferedImage implements Raster {
    private final BufferedImage img;
    private int color;

    public RasterBufferedImage(int width, int height) {
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public void setPixel(int x, int y, int color) {
        try {
            img.setRGB(x, y, color);
        } catch (Exception ignored) {
        }
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

    @Override
    public void clear() {
        Graphics gr = img.getGraphics();
        gr.setColor(Color.BLACK);
        gr.fillRect(0, 0, getWidth(), getHeight());
    }

    public void clear(Color color) {
        Graphics gr = img.getGraphics();
        gr.setColor(color);
        gr.fillRect(0, 0, getWidth(), getHeight());
    }

    public BufferedImage getImg() {
        return img;
    }

    public Graphics getGraphics(){
        return img.getGraphics();
    }

    public void repaint(Graphics graphics) {
        graphics.drawImage(img, 0, 0, null);
    }

    public void draw(RasterBufferedImage raster) {
        Graphics graphics = getGraphics();
        graphics.setColor(new Color(color));
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.drawImage(raster.img, 0, 0, null);
    }

    @Override
    public void setClearColor(int color) {
        this.color = color;
    }

    @Override
    public int getWidth() {
        return img.getWidth();
    }

    @Override
    public int getHeight() {
        return img.getHeight();
    }

    public RasterBufferedImage cloneRaster(RasterBufferedImage raster) {
        int width = raster.getImg().getWidth();
        int height = raster.getImg().getHeight();
        RasterBufferedImage r = new RasterBufferedImage(width, height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                r.setPixel(x, y, raster.getImg().getRGB(x, y));
            }
        }

        return r;
    }
}
