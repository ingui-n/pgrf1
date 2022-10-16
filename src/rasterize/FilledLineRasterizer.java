package rasterize;

public class FilledLineRasterizer extends LineRasterizer {
    public FilledLineRasterizer(Raster raster) {
        super(raster);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
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
                raster.setPixel(x1, y, this.color);
            }
        } else if (y1 == y2) {
            for (int x = x1; x <= x2; x++) {
                raster.setPixel(x, y1, this.color);
            }
        } else {
            for (int x = x1; x <= x2; x++) {
                float y = k * x + q;
                raster.setPixel(x, Math.round(y), this.color);
            }

            for (int y = y1; y <= y2; y++) {
                float x = (y - q) / k;
                raster.setPixel(Math.round(x), y, this.color);
            }
        }
    }
}
