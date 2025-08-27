package GUI.shapesGUI;

import GUI.Constants;
import GUI.ScreenTransform;
import GUI.elevation.ElevationSlider;
import logic.physics.Block;
import logic.physics.Position;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

public class BlockGUI implements ShapeGUI, Serializable {

    public final Block block;
    private final Color color;

    public BlockGUI(Polygon polygon, double height) {
        this.block = new Block(polygon, height);
        this.color = ElevationSlider.getElevationColor(height);
    }

    public double getHeight() {
        return block.height;
    }

    @Override
    public boolean contains(int x, int y) {
        return block.polygon.contains(x, y);
    }

    @Override
    public void drawShape(Graphics2D g, ScreenTransform transform) {
        Polygon drawPolygon = new Polygon();
        AffineTransform at = new AffineTransform();
        at.setToTranslation(transform.x(), transform.y());
        at.scale(transform.zoom(), transform.zoom());   // TODO: really not sure this works

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
