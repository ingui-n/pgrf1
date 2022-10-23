package view;

import rasterize.*;
import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Panel extends JPanel {
    private RasterBufferedImage raster;

    private String mode = "line";

    private static final int FPS = 30;

    Panel(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        raster = new RasterBufferedImage(width, height);

        raster.setClearColor(Color.BLACK.getRGB());
        setLoop();
    }

    public RasterBufferedImage getRaster() {
        return raster;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public boolean isNotMode(String mode) {
        return !this.mode.equals(mode);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        raster.repaint(g);
    }

    public void resize() {
        if (this.getWidth() < 1 || this.getHeight() < 1)
            return;
        if (this.getWidth() <= raster.getWidth() && this.getHeight() <= raster.getHeight()) //no resize if new is smaller
            return;
        RasterBufferedImage newRaster = new RasterBufferedImage(this.getWidth(), this.getHeight());

        newRaster.draw(raster);
        raster = newRaster;
    }

    private void setLoop() {
        // časovač, který 30 krát za vteřinu obnoví obsah plátna aktuálním img
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, 0, FPS);
    }

    public void clear() {
        raster.clear();
        printLegend();
    }

    public void printLegend() {
        Graphics gr = raster.getGraphics();

        if (this.mode.equals("line"))
            gr.setColor(Color.CYAN);

        gr.drawString("Lines [L]", 5, 15);
        gr.setColor(Color.WHITE);

        if (this.mode.equals("polygon"))
            gr.setColor(Color.CYAN);

        gr.drawString("Polygon [P]", 5, 30);
        gr.setColor(Color.WHITE);

        if (this.mode.equals("triangle"))
            gr.setColor(Color.CYAN);

        gr.drawString("Triangle [T]", 5, 45);
        gr.setColor(Color.WHITE);

        gr.drawString("Clear [C]", 5, 60);
    }
}
