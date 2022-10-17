import model.Line;
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
    //private static RasterBufferedImage rasterCopy;
    private static PolygonRasterizer polygonRasterizer;
    private static int currentMouseButton = -1;

    public static void main(String[] args) {
        Window window = new Window();
        panel = window.getPanel();
        raster = panel.getRaster();

        Polygon polygon = new Polygon();
        lineRasterizer = new FilledLineRasterizer(raster);
        polygonRasterizer = new PolygonRasterizer(lineRasterizer);
        panel.printLegend();

        window.setVisible(true);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                currentMouseButton = e.getButton();

                if (currentMouseButton != MouseEvent.BUTTON1)
                    return;

                lineRasterizer.setColor(Color.RED);
                Point point = new Point(e.getX(), e.getY());
                polygon.addPoint(point);

                panel.clear();
                polygonRasterizer.rasterize(polygon);
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                currentMouseButton = e.getButton();

                if (currentMouseButton != MouseEvent.BUTTON1)
                    return;

                Point point = new Point(e.getX(), e.getY());
                polygon.addPoint(point);
                polygonRasterizer.rasterize(polygon);
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (currentMouseButton != MouseEvent.BUTTON1)
                    return;

                polygon.removeLastPoint();

                Point point = new Point(e.getX(), e.getY());
                polygon.addPoint(point);

                panel.clear();
                polygonRasterizer.rasterize(polygon);
            }
        });

        panel.addComponentListener(new ComponentAdapter() {
            /**
             * Resets raster and all rasterizerS
             * @param e the event to be processed
             */
            @Override
            public void componentResized(ComponentEvent e) {
                raster = panel.getRaster();
                lineRasterizer = new FilledLineRasterizer(raster);
                polygonRasterizer = new PolygonRasterizer(lineRasterizer);
            }
        });
    }
}
