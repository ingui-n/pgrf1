package rasterize;

import model.Line;
import model.Point;
import model.Triangle;

public class TriangleRasterizer {
    private final LineRasterizer lineRasterizer;

    public TriangleRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    public void rasterize(Triangle triangle) {
        if (triangle.getLineAB() != null) {
            lineRasterizer.rasterize(triangle.getLineAB());
        }

        Point pointC = triangle.getPointC();

        if (pointC == null)
            return;

        Point pointA = triangle.getPointA();
        Point pointB = triangle.getPointB();
        Point pointS = triangle.getPointS();

        Line sc = new Line(pointS, pointC);
        sc.setType("dashed");

        lineRasterizer.rasterize(new Line(pointA, pointC));
        lineRasterizer.rasterize(new Line(pointB, pointC));
        lineRasterizer.rasterize(sc);
    }
}
