package model;

import java.util.ArrayList;

public class Polygon {
    private final ArrayList<Point> points;

    public Polygon() {
        this.points = new ArrayList<>();
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public void addPoint(Point point, int index) {
        points.add(index, point);
    }

    public void replacePoint(Point point, int index) {
        points.remove(index);
        points.add(index, point);
    }

    public Point getPoint(int index) {
        return points.get(index);
    }

    public int getCount() {
        return points.size();
    }

    public void removeLastPoint() {
        points.remove(getCount() - 1);
    }

    public void changePointInPolygon(int mouseX, int mouseY) {
        Point closestPoint = getPoint(0);
        int closestPointIndex = 0;

        for (int i = 1; i < getCount(); i++) {
            Point p = getPoint(i);

            boolean isXCloser = Math.abs(mouseX - closestPoint.getX()) > Math.abs(mouseX - p.getX());
            boolean isYCloser = Math.abs(mouseY - closestPoint.getY()) > Math.abs(mouseY - p.getY());

            if (isXCloser && isYCloser) {
                closestPoint = p;
                closestPointIndex = i;
            }
        }

        Point point = new Point(mouseX, mouseY);
        replacePoint(point, closestPointIndex);
    }
}
