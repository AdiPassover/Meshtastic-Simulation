package GUI.shapesGUI;

import GUI.Constants;
import GUI.elevation.ElevationSlider;
import logic.shapes.Position;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class BlockGUI implements ShapeGUI {

    private final Polygon polygon;
    private final double height;
    private final Color color;

    public BlockGUI(Polygon polygon, double height) {
        this.polygon = polygon;
        this.height = height;
        this.color = ElevationSlider.getElevationColor(height);
    }

    public double getHeight() {
        return height;
    }

    public boolean intersectsLine(Position p1, Position p2) {
        double blockHeight = getHeight();

        double x1 = p1.x, y1 = p1.y, z1 = p1.z;
        double x2 = p2.x, y2 = p2.y, z2 = p2.z;

        Line2D.Double segment2D = new Line2D.Double(x1, y1, x2, y2);

        int[] xs = polygon.xpoints;
        int[] ys = polygon.ypoints;
        int n = polygon.npoints;

        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            Line2D.Double edge = new Line2D.Double(xs[i], ys[i], xs[j], ys[j]);

            if (segment2D.intersectsLine(edge)) {
                // Compute intersection point
                Point2D.Double intersection = getLineIntersection(
                        x1, y1, x2, y2,
                        xs[i], ys[i], xs[j], ys[j]
                );
                if (intersection != null) {
                    // Get param t (0 to 1) along the original 3D segment
                    double dx = x2 - x1;
                    double dy = y2 - y1;
                    double t = Math.hypot(intersection.x - x1, intersection.y - y1) /
                            Math.hypot(dx, dy);

                    double z = z1 + t * (z2 - z1);
                    if (z <= blockHeight) {
                        return true; // Blocked
                    }
                }
            }
        }

        // Also check if the segment starts or ends inside the polygon
        if (polygon.contains(x1, y1) && z1 <= blockHeight) return true;
        if (polygon.contains(x2, y2) && z2 <= blockHeight) return true;

        return false;
    }

    private Point2D.Double getLineIntersection(
            double x1, double y1, double x2, double y2,
            double x3, double y3, double x4, double y4
    ) {
        double denom = (y4 - y3)*(x2 - x1) - (x4 - x3)*(y2 - y1);
        if (denom == 0) return null; // parallel

        double ua = ((x4 - x3)*(y1 - y3) - (y4 - y3)*(x1 - x3)) / denom;

        double x = x1 + ua * (x2 - x1);
        double y = y1 + ua * (y2 - y1);

        return new Point2D.Double(x, y);
    }



    @Override
    public boolean contains(int x, int y) {
        return polygon.contains(x, y);
    }

    @Override
    public void drawShape(Graphics2D g) {
        g.setColor(color);
        g.fill(polygon);

        g.setColor(Constants.BLOCK_OUTLINE_COLOR);
        g.draw(polygon);
    }

}
