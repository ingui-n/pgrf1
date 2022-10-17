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
    private static Polygon polygon;
    private static int currentMouseButton = -1;

    public static void main(String[] args) {
        Window window = new Window();
        panel = window.getPanel();
        raster = panel.getRaster();

        polygon = new Polygon();
        lineRasterizer = new FilledLineRasterizer(raster);
        polygonRasterizer = new PolygonRasterizer(lineRasterizer);
        panel.printLegend();
        lineRasterizer.setColor(Color.RED);

        window.setVisible(true);

        extendPolygonListeners();
        polygonPointShiftListeners();


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

    private static void polygonPointShiftListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                currentMouseButton = e.getButton();

                if (currentMouseButton != MouseEvent.BUTTON3)
                    return;

                polygon.changePointInPolygon(e.getX(), e.getY());
                panel.clear();
                polygonRasterizer.rasterize(polygon);

                //get nejblizsi 2 nebo 1 bod
                //uloz na index
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (currentMouseButton != MouseEvent.BUTTON3)
                    return;

                polygon.changePointInPolygon(e.getX(), e.getY());
                panel.clear();
                polygonRasterizer.rasterize(polygon);
            }
        });
    }

    private static void extendPolygonListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                currentMouseButton = e.getButton();

                if (currentMouseButton != MouseEvent.BUTTON1)
                    return;

                Point point = new Point(e.getX(), e.getY());
                polygon.addPoint(point);

                panel.clear();
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
    }
}
