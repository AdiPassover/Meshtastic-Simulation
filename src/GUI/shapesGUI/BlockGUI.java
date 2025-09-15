package GUI.shapesGUI;

import GUI.Constants;
import GUI.ScreenTransform;
import GUI.elevation.ElevationSlider;
import logic.physics.Block;
import logic.physics.Position;

import java.awt.*;
import java.io.Serializable;

public class BlockGUI implements ShapeGUI, Serializable {

    public final Block block;
    private final Color color;

    public BlockGUI(Polygon polygon, double height) {
        this.block = new Block(polygon, height);
        this.color = ElevationSlider.getElevationColor(height);
    }
    public BlockGUI(Block block) { this(block.polygon, block.height); }

    public double getHeight() {
        return block.height;
    }

    @Override
    public boolean contains(int x, int y, ScreenTransform transform) {
        return block.contains(transform.screenToWorld(new Point(x, y)));
    }

    @Override
    public void drawShape(Graphics2D g, ScreenTransform transform) {
        Polygon drawPolygon = new Polygon();

        for (int i = 0; i < block.polygon.npoints; ++i) {
            Position worldPosition = new Position(block.polygon.xpoints[i], block.polygon.ypoints[i]);
            Point drawPoint = transform.worldToScreen(worldPosition);
            drawPolygon.addPoint(drawPoint.x, drawPoint.y);
        }

        g.setColor(color);
        g.fill(drawPolygon);

        g.setColor(Constants.BLOCK_OUTLINE_COLOR);
        g.draw(drawPolygon);
    }

}
