package rasterize;

import java.awt.*;

public interface Raster {
    void setPixel(int x, int y, Color color);
    int getPixel(int x, int y);
}
