package GUI.shapesGUI;

import GUI.Constants;
import GUI.elevation.ElevationSlider;
import logic.physics.Block;

import java.awt.*;
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
    public void drawShape(Graphics2D g) {
        g.setColor(color);
        g.fill(block.polygon);

        g.setColor(Constants.BLOCK_OUTLINE_COLOR);
        g.draw(block.polygon);
    }

}
