package model;

import java.util.ArrayList;

public class Polygon {
    private ArrayList<Point> points;

    public Polygon() {
        this.points = new ArrayList<>();
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public Point getPoint(int index) {
        return points.get(index);
    }

    public int getCount() {
        return points.size();
    }
}
