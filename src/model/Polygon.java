package model;

import java.util.ArrayList;

public class Polygon {
    private final ArrayList<Point> points;
    private int movePointIndex = -1;

    public Polygon() {
        this.points = new ArrayList<>();
    }

    public Point getPoint(int index) {
        return points.get(index);
    }

    public Point getNextPoint(int index) {
        return getCount() > 1 ? getCount() == index + 1 ? points.get(0) : points.get(index + 1) : null;
    }

    public Point getPreviousPoint(int index) {
        return getCount() > 1 ? index == 0 ? points.get(getCount() - 1) : getPoint(index - 1) : null;
    }

    public int getCount() {
        return points.size();
    }

    public int getMovePointIndex() {
        return movePointIndex;
    }

    public void setMovePointIndex(int movePointIndex) {
        this.movePointIndex = movePointIndex;
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

    public void removeLastPoint() {
        points.remove(getCount() - 1);
    }

    public void removePoint(Point point) {
        points.remove(point);
    }

    public void clear() {
        points.clear();
    }

    public Point getClosestPoint(int x, int y) {
        double distance = points.get(0).countDistance(x, y);
        int pointIndex = 0;

        for (int i = 1; i < getCount(); i++) {
            double testDistance = points.get(i).countDistance(x, y);

            if (distance > testDistance) {
                pointIndex = i;
                distance = testDistance;
            }
        }

        return points.get(pointIndex);
    }

    public int getPointIndex(Point point) {
        for (int i = 0; i <= getCount(); i++) {
            if (getPoint(i).equals(point))
                return i;
        }

        return -1;
    }

    public void moveClosestPointInPolygon(int mouseX, int mouseY) {
        if (getCount() == 0)
            return;

        Point closestPoint = getClosestPoint(mouseX, mouseY);

        Line polygonPointToMouse = new Line(closestPoint.getX(), closestPoint.getY(), mouseX, mouseY);

        if (polygonPointToMouse.getLineLength() > 20)
            return;

        int closestPointIndex = getPointIndex(closestPoint);
        movePointIndex = closestPointIndex;

        Point point = new Point(mouseX, mouseY);
        replacePoint(point, closestPointIndex);
    }
}
