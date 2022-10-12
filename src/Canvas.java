import model.Polygon;
import rasterize.*;
import model.Line;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import model.Point;

public class Canvas {
    private final JFrame frame;
    private JPanel panel;
    private RasterBufferImage raster;
    private LineRasterizer lineRasterizer;
    private PolygonRasterizer polygonRasterizer;

    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private RasterBufferImage r;

    private Polygon polygon;

    public Canvas(int width, int height) {
        frame = new JFrame();

        frame.setLayout(new BorderLayout());
        frame.setTitle("PGRF1");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        raster = new RasterBufferImage(800, 600);
        lineRasterizer = new LineRasterizerGraphics(raster);
        //lineRasterizer = new LineRasterizerTrivial(raster);
        polygonRasterizer = new PolygonRasterizer(lineRasterizer);

        polygon = new Polygon();

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                raster.present(g);
            }
        };

        panel.setPreferredSize(new Dimension(width, height));

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        panel.requestFocus();
        panel.requestFocusInWindow();

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point point = new Point(e.getX(), e.getY());
                polygon.addPoint(point);

                raster.clear();
                polygonRasterizer.rasterize(polygon);
                panel.repaint();
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x1 = e.getX();
                y1 = e.getY();

                r = cloneRaster(raster);

            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //raster.clear();

                Line line = new Line(x1, y1, x2, y2, Color.BLACK);
                lineRasterizer.rasterize(line);

                x2 = e.getX();
                y2 = e.getY();

                Line blackLine = new Line(x1, y1, x2, y2, new Color(0xFF0000));
                lineRasterizer.rasterize(blackLine);
                panel.repaint();
            }
        });

        /* panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                raster = r;
                lineRasterizer = new LineRasterizerTrivial(raster);

                panel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        raster.present(g);
                    }
                };
            }
        });*/

        /*
         * paint from the origin

         panel.addMouseMotionListener(new MouseAdapter() {
        @Override public void mouseDragged(MouseEvent e) {
        raster.clear();

        Line line = new Line(width / 2, height / 2, e.getX(), e.getY(), 0xff0000);
        lineRasterizer.rasterize(line);

        panel.repaint();
        }
        });*/
    }

    private RasterBufferImage cloneRaster(RasterBufferImage raster) {
        int width = raster.getImg().getWidth();
        int height = raster.getImg().getHeight();
        RasterBufferImage r = new RasterBufferImage(width, height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                r.setPixel(x, y, new Color(raster.getImg().getRGB(x, y)));
            }
        }

        return r;
    }
}
