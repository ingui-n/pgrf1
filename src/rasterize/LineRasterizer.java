package rasterize;
import model.Line;
import java.awt.*;


public abstract class LineRasterizer {
    protected Raster raster;

    public LineRasterizer(Raster raster) {
        this.raster = raster;
    }

    public void rasterize(Line line) {
        drawLine(line.getX1(), line.getY1(), line.getX2(), line.getY2(), line.getColor(), line.getType());
    }

    public void drawLine(int x1, int y1, int x2, int y2, Color color, String type) {
    }
}
