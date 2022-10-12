package rasterize;

import java.awt.*;

public class LineRasterizerTrivial extends LineRasterizer {

    public LineRasterizerTrivial(Raster raster) {
        super(raster);
    }

    @Override
    protected void drawLine(int x1, int y1, int x2, int y2, Color color) {
        float k = (y2 - y1) / (float) (x2 - x1);
        float q = y1 - k * x1;

        if (x2 <= x1) {
            int _x1 = x1;
            x1 = x2;
            x2 = _x1;
        }

        if (y2 <= y1) {
            int _y1 = y1;
            y1 = y2;
            y2 = _y1;
        }

        if (x1 == x2) {
            for (int y = y1; y <= y2; y++) {
                raster.setPixel(x1, y, color);
            }
        } else if (y1 == y2) {
            for (int x = x1; x <= x2; x++) {
                raster.setPixel(x, y1, color);
            }
        } else {
            for (int x = x1; x <= x2; x++) {
                float y = k * x + q;
                raster.setPixel(x, Math.round(y), color);
            }

            for (int y = y1; y <= y2; y++) {
                float x = (y - q) / k;
                raster.setPixel(Math.round(x), y, color);
            }
        }
    }
}
