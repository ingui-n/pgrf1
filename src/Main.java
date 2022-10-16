import model.Point;
import model.Polygon;
import rasterize.FilledLineRasterizer;
import rasterize.LineRasterizer;
import rasterize.PolygonRasterizer;
import rasterize.RasterBufferedImage;
import view.Panel;
import view.Window;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main {
    private static Panel panel;
    private static LineRasterizer lineRasterizer;
    private static RasterBufferedImage raster;
    private static PolygonRasterizer polygonRasterizer;

    public static void main(String[] args) {
        Window window = new Window();
        panel = window.getPanel();
        raster = panel.getRaster();

        Polygon polygon = new Polygon();
        lineRasterizer = new FilledLineRasterizer(raster);
        polygonRasterizer = new PolygonRasterizer(lineRasterizer);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                lineRasterizer.setColor(Color.RED);
                Point point = new Point(e.getX(), e.getY());
                polygon.addPoint(point);

                raster.clear(Color.BLACK);
                polygonRasterizer.rasterize(polygon);
                panel.repaint();
            }
        });

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                raster = panel.getRaster();
                lineRasterizer = new FilledLineRasterizer(raster);
                polygonRasterizer = new PolygonRasterizer(lineRasterizer);
            }
        });
    }
}
