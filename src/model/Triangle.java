package model;

public class Triangle {
    private Point pointA;
    private Point pointB;
    private Point pointC;
    private Point pointS;
    private Point firstPoint;
    private Point secondPoint;
    private Line lineAB;

    public Point getPointA() {
        return pointA;
    }

    public Point getPointB() {
        return pointB;
    }

    public Point getPointC() {
        return pointC;
    }

    public Point getPointS() {
        return pointS;
    }

    public Line getLineAB() {
        return lineAB;
    }

    public Point getFirstPoint() {
        return firstPoint;
    }

    public void setFirstPoint(Point firstPoint) {
        this.firstPoint = firstPoint;
    }

    public void setSecondPoint(Point secondPoint) {
        this.secondPoint = secondPoint;
    }

    public void calculateLineAB() {
        if (this.firstPoint.getX() > this.secondPoint.getX()) {
            this.pointA = this.firstPoint;
            this.pointB = this.secondPoint;
        } else {
            this.pointA = this.secondPoint;
            this.pointB = this.firstPoint;
        }

        this.lineAB = new Line(this.pointA, this.pointB);
        this.pointS = this.lineAB.getCenter();
    }

    public void calculatePointC(int mouseX, int mouseY) {
        if (this.pointA == null || this.pointB == null)
            return;

        // normála X a Y
        double normalABx = this.pointB.getY() - this.pointA.getY();
        double normalABy = Math.negateExact(this.pointB.getX() - this.pointA.getX());

        // hodnota C rovnice přímky
        int ABc = Math.negateExact((int) (this.pointA.getX() * normalABx + this.pointA.getY() * normalABy));

        // rovnice přímky = normálaX * x + normálaY * y + c
        double ABy = normalABx * mouseX + normalABy * mouseY + ABc;

        // vzdálenost myši od přímky
        int distanceSC = (int) (Math.abs(ABy) / Math.sqrt(Math.pow(normalABx, 2) + Math.pow(normalABy, 2)));

        double vectorABx, vectorABy;

        // pokud je rovnicePřímky < 0 otoč bod A a B a vypočítej normálový vektor
        if (ABy > 0) {
            vectorABx = normalABx;
            vectorABy = normalABy;
        } else {
            vectorABx = this.pointA.getY() - this.pointB.getY();
            vectorABy = Math.negateExact(this.pointA.getX() - this.pointB.getX());
        }

        // jednotkový vektor
        double unitVectorABx = vectorABx / this.lineAB.getLineLength();
        double unitVectorABy = vectorABy / this.lineAB.getLineLength();

        // bod C = středAB + jednotkovýVektor * vzdálenostSC
        this.pointC = new Point(this.pointS.getX() + unitVectorABx * distanceSC, this.pointS.getY() + unitVectorABy * distanceSC);
    }

    public void clear() {
        this.pointA = null;
        this.pointB = null;
        this.pointC = null;
        this.pointS = null;
        this.lineAB = null;
        this.firstPoint = null;
        this.secondPoint = null;
    }
}
