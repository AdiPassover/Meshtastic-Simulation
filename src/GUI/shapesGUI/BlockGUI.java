package GUI.shapesGUI;

import GUI.Constants;
import GUI.elevation.ElevationSlider;

import java.awt.*;

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
