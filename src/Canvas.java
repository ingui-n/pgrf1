import model.Line;
import model.Point;
import model.Polygon;
import model.Triangle;
import rasterize.*;
import view.Panel;
import view.Window;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Canvas {
    private final Panel panel;
    private LineRasterizer lineRasterizer;
    private PolygonRasterizer polygonRasterizer;
    private TriangleRasterizer triangleRasterizer;
    private RasterBufferedImage raster;
    private final ArrayList<Line> lines = new ArrayList<>();
    private final Polygon polygon;
    private final Triangle triangle;
    private int currentMouseButton = -1;
    private Point polygonClosestPoint;
    private int polygonClosestPointIndex;
    private Point linePoint1;

    public Canvas(int width, int height) {
        Window window = new Window(width, height);
        panel = window.getPanel();
        raster = panel.getRaster();

        polygon = new Polygon();
        triangle = new Triangle();
        lineRasterizer = new FilledLineRasterizer(raster);
        polygonRasterizer = new PolygonRasterizer(lineRasterizer);
        triangleRasterizer = new TriangleRasterizer(lineRasterizer);
        window.setVisible(true);

        lineListeners();
        polygonListeners();
        triangleListeners();

        keyEventListeners();
        resizeListener();
    }

    private void lineListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (panel.isNotMode("line"))
                    return;

                currentMouseButton = e.getButton();

                if (currentMouseButton != MouseEvent.BUTTON1)
                    return;

                linePoint1 = new Point(e.getX(), e.getY());
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (panel.isNotMode("line"))
                    return;

                if (currentMouseButton != MouseEvent.BUTTON1)
                    return;

                if (linePoint1 == null)
                    return;

                Point linePoint2 = new Point(e.getX(), e.getY());
                Line line = new Line(linePoint1, linePoint2);
                line.setColor(Color.CYAN);
                line.setType("dotted");

                lines.add(line);

                panel.clear();

                for (Line l : lines) {
                    lineRasterizer.rasterize(l);
                }

                lines.remove(line);
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (panel.isNotMode("line"))
                    return;

                currentMouseButton = e.getButton();

                if (currentMouseButton != MouseEvent.BUTTON1)
                    return;

                if (linePoint1 == null)
                    return;

                int mouseX = e.getX();
                int mouseY = e.getY();

                if (linePoint1.getX() == mouseX && linePoint1.getY() == mouseY)
                    return;

                Point linePoint2 = new Point(mouseX, mouseY);
                Line line = new Line(linePoint1, linePoint2);

                lines.add(line);

                for (Line l : lines) {
                    lineRasterizer.rasterize(l);
                }
            }
        });
    }

    private void polygonListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (panel.isNotMode("polygon"))
                    return;

                int mouseX = e.getX();
                int mouseY = e.getY();

                currentMouseButton = e.getButton();

                if (currentMouseButton == MouseEvent.BUTTON3) {
                    polygon.moveClosestPointInPolygon(mouseX, mouseY);

                    panel.clear();
                    polygonRasterizer.rasterize(polygon);
                } else if (currentMouseButton == MouseEvent.BUTTON1) {
                    if (polygon.getCount() < 2)
                        return;

                    polygonClosestPoint = polygon.getClosestPoint(mouseX, mouseY);
                    polygonClosestPointIndex = polygon.getPointIndex(polygonClosestPoint);
                } else if (currentMouseButton == MouseEvent.BUTTON2) {
                    polygon.removeClosestPoint(mouseX, mouseY);

                    panel.clear();
                    polygonRasterizer.rasterize(polygon);
                }
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (panel.isNotMode("polygon"))
                    return;

                int mouseX = e.getX();
                int mouseY = e.getY();

                if (currentMouseButton == MouseEvent.BUTTON3) {
                    int polygonMovePointIndex = polygon.getMovePointIndex();

                    if (polygonMovePointIndex != -1) {
                        Point point = new Point(mouseX, mouseY);
                        polygon.replacePoint(point, polygonMovePointIndex);

                        panel.clear();
                        polygonRasterizer.rasterize(polygon);
                    }
                } else if (currentMouseButton == MouseEvent.BUTTON1) {
                    if (polygon.getCount() < 2)
                        return;

                    Point point = polygon.getPreviousPoint(polygonClosestPointIndex);

                    Line line1 = new Line(polygonClosestPoint.getX(), polygonClosestPoint.getY(), mouseX, mouseY);
                    line1.setType("dashed");
                    line1.setColor(Color.CYAN);

                    Line line2 = new Line(point.getX(), point.getY(), mouseX, mouseY);
                    line2.setType("dashed");
                    line2.setColor(Color.CYAN);

                    panel.clear();
                    polygonRasterizer.rasterize(polygon);
                    lineRasterizer.rasterize(line1);
                    lineRasterizer.rasterize(line2);
                }
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (panel.isNotMode("polygon"))
                    return;

                currentMouseButton = e.getButton();

                if (currentMouseButton == MouseEvent.BUTTON3) {
                    polygon.setMovePointIndex(-1);
                } else if (currentMouseButton == MouseEvent.BUTTON1) {
                    int mouseX = e.getX();
                    int mouseY = e.getY();

                    Point point = new Point(mouseX, mouseY);
                    polygon.addPoint(point, polygon.getCount() < 2 ? polygon.getCount() : polygonClosestPointIndex);

                    panel.clear();
                    polygonRasterizer.rasterize(polygon);
                }
            }
        });
    }

    private void triangleListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (panel.isNotMode("triangle"))
                    return;

                currentMouseButton = e.getButton();

                if (currentMouseButton != MouseEvent.BUTTON1)
                    return;

                if (triangle.getPointC() != null) {
                    panel.removeMouseMotionListener(drawTriangleListener);
                    triangle.clear();
                } else {
                    triangle.setFirstPoint(new Point(e.getX(), e.getY()));
                }
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (panel.isNotMode("triangle"))
                    return;

                if (currentMouseButton != MouseEvent.BUTTON1)
                    return;

                if (triangle.getFirstPoint() == null)
                    return;

                triangle.setSecondPoint(new Point(e.getX(), e.getY()));
                triangle.calculateLineAB();

                panel.clear();
                triangleRasterizer.rasterize(triangle);
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (panel.isNotMode("triangle"))
                    return;

                if (triangle.getLineAB() == null)
                    return;

                panel.addMouseMotionListener(drawTriangleListener);
            }
        });
    }

    MouseMotionListener drawTriangleListener = new MouseAdapter() {
        @Override
        public void mouseMoved(MouseEvent e) {
            if (triangle.getLineAB() == null)
                return;

            triangle.calculatePointC(e.getX(), e.getY());

            panel.clear();
            triangleRasterizer.rasterize(triangle);
        }
    };

    private void keyEventListeners() {
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    clearAllStructures();
                } else if (e.getKeyCode() == KeyEvent.VK_P) {
                    panel.setMode("polygon");
                    clearAllStructures();
                } else if (e.getKeyCode() == KeyEvent.VK_L) {
                    panel.setMode("line");
                    clearAllStructures();
                } else if ((e.getKeyCode() == KeyEvent.VK_T)) {
                    panel.setMode("triangle");
                    clearAllStructures();
                }
            }
        });
    }

    private void resizeListener() {
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
                triangleRasterizer = new TriangleRasterizer(lineRasterizer);
            }
        });
    }

    private void clearAllStructures() {
        panel.removeMouseMotionListener(drawTriangleListener);
        currentMouseButton = -1;
        polygonClosestPoint = null;
        linePoint1 = null;
        lines.clear();
        polygon.clear();
        triangle.clear();
        panel.clear();
    }

    public void start() {
        raster.clear(Color.BLACK);
        panel.repaint();
        panel.printLegend();
    }
}
