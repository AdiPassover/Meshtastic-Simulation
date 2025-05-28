package GUI.shapesGUI;

import GUI.Constants;
import logic.graph_objects.Edge;

import java.awt.*;

public class EdgeGUI implements ShapeGUI {

    public final NodeGUI node1, node2;

    public EdgeGUI(NodeGUI node1, NodeGUI node2) {
        this.node1 = node1;
        this.node2 = node2;
    }


    @Override
    public boolean contains(int x, int y) {
        return false;
    }

    @Override
    public void drawShape(Graphics2D g) {
        g.setColor(Constants.EDGE_COLOR);
        g.setStroke(Constants.EDGE_STROKE);
        g.drawLine(
                (int) node1.node.x(), (int) node1.node.y(),
                (int) node2.node.x(), (int) node2.node.y()
        );
    }
}
