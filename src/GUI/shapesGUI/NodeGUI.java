package GUI.shapesGUI;

import GUI.Constants;
import logic.graph_objects.Node;
import logic.shapes.Position;

import java.awt.*;

public class NodeGUI implements ShapeGUI {

    public final Node node;
    private Color color = Constants.NODE_COLOR;

    public NodeGUI(int id, Position position) {
        node = new Node(id, position);
    }


    @Override
    public boolean contains(int x, int y) {
        return node.position.distance2D() <= Constants.NODE_RADIUS;
    }


    @Override
    public void drawShape(Graphics2D g) {
        g.setColor(color);
        int size = Constants.NODE_RADIUS;
        g.fillOval((int)node.x()-size, (int)node.y()-size, 2*size, 2*size);
        g.setColor(Constants.NODE_TEXT_COLOR);
        g.setFont(Constants.NODE_FONT);
        g.drawString(Integer.toString(node.id), (int)node.x(), (int)node.y());
    }

}
