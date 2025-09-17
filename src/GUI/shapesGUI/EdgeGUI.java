package GUI.shapesGUI;

import GUI.GUIConstants;
import GUI.ScreenTransform;
import logic.physics.Position;

import java.awt.*;

public class EdgeGUI implements ShapeGUI {

    public final NodeGUI node1, node2;

    public EdgeGUI(NodeGUI node1, NodeGUI node2) {
        this.node1 = node1;
        this.node2 = node2;
    }


    @Override
    public boolean contains(int x, int y, ScreenTransform transform) {
        return false;
    }

    @Override
    public void drawShape(Graphics2D g, ScreenTransform transform) {
        g.setColor(GUIConstants.EDGE_COLOR);
        g.setStroke(GUIConstants.EDGE_STROKE); // TODO: adjust by zoom? (A: not necessary IMO)
        Point drawLoc1 = transform.worldToScreen(new Position(node1.node.x(), node1.node.y()));
        Point drawLoc2 = transform.worldToScreen(new Position(node2.node.x(), node2.node.y()));

        g.drawLine(
                drawLoc1.x, drawLoc1.y,
                drawLoc2.x, drawLoc2.y
        );
    }
}
