package rasterize;


import java.awt.*;

public class LineRasterizerGraphics extends LineRasterizer{

    public LineRasterizerGraphics(Raster raster) {
        super(raster);
    }

    @Override
    protected void drawLine(int x1, int y1, int x2, int y2, Color color) {
        Graphics g = ((RasterBufferImage)raster).getImg().getGraphics();
        g.setColor(color);
        g.drawLine(x1, y1, x2, y2);
    }
}
