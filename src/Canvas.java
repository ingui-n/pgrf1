import model.Polygon;
import rasterize.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import model.Point;

public class Canvas {
    private final JFrame frame;
    private JPanel panel;
    private RasterBufferedImage raster;
    private LineRasterizer lineRasterizer;
    private PolygonRasterizer polygonRasterizer;

    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private RasterBufferedImage r;

    private Polygon polygon;

    public Canvas(int width, int height) {
        frame = new JFrame();

        frame.setLayout(new BorderLayout());
        frame.setTitle("PGRF1");
        frame.setResizable(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        raster = new RasterBufferedImage(width, height);

//        lineRasterizer = new LineRasterizerGraphics(raster);
        lineRasterizer = new FilledLineRasterizer(raster);

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
                lineRasterizer.setColor(Color.RED);
                Point point = new Point(e.getX(), e.getY());
                polygon.addPoint(point);

                raster.clear(Color.BLACK);
                polygonRasterizer.rasterize(polygon);
                panel.repaint();
            }
        });

        /*panel.addMouseListener(new MouseAdapter() {
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
        });*/

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

    private RasterBufferedImage cloneRaster(RasterBufferedImage raster) {
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

    public void start() {
        raster.clear(Color.BLACK);
        raster.getGraphics().drawString("Select mode", 5, 15);
        panel.repaint();
    }

    public static void main(String[] args) {
        new Canvas(800, 600).start();
    }
}
