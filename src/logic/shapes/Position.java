package logic.shapes;

import java.io.Serializable;

public class Position implements Serializable {

    public final double x;
    public final double y;
    public final double z;

    public Position(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Position(double x, double y) { this(x, y, 0.0); }
    public Position() { this(0.0, 0.0, 0.0); }


    public double distance(double x1, double y1, double z1) {
        return Math.sqrt(Math.pow(x1 - x, 2) + Math.pow(y1 - y, 2) + Math.pow(z1 - z, 2));
    }
    public double distance(Position other) {
        return distance(other.x, other.y, other.z);
    }
    public double distance2D(int x1, int y1) {
        return Math.sqrt(Math.pow(x1 - x, 2) + Math.pow(y1 - y, 2));
    }
    public double distance2D(Position other) {
        return Math.sqrt(Math.pow(other.x - x, 2) + Math.pow(other.y - y, 2));
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

}
