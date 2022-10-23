package rasterize;

import model.Line;
import model.Point;
import model.Polygon;

public class PolygonRasterizer {
    private final LineRasterizer lineRasterizer;

    public PolygonRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    public void rasterize(Polygon polygon) {
        for (int i = 0; i < polygon.getCount(); i++) {
            Point point1 = polygon.getPoint(i);
            Point point2 = polygon.getPoint(i + 1 < polygon.getCount() ? i + 1 : 0);

            lineRasterizer.rasterize(new Line(point1, point2));
        }
    }
}
