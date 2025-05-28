package GUI.shapesGUI;

import GUI.Constants;
import logic.graph_objects.Node;
import logic.shapes.Position;

import java.awt.*;

public class NodeGUI implements ShapeGUI {

    public final Node node;

    public NodeGUI(int id, Position position) {
        node = new Node(id, position);
    }

    public Position getPosition() { return node.position; }
    public int getId() { return node.id; }


    @Override
    public boolean contains(int x, int y) {
        return node.position.distance2D() <= Constants.NODE_RADIUS;
    }


    @Override
    public void drawShape(Graphics2D g) {
        g.setColor(Constants.NODE_COLOR);
        int size = Constants.NODE_RADIUS;
        g.fillOval((int)node.x()-size, (int)node.y()-size, 2*size, 2*size);
        g.setColor(Constants.NODE_TEXT_COLOR);
        g.setFont(Constants.NODE_FONT);

        g.drawString(Integer.toString(node.id), (int)node.x()-size/4, (int)node.y());
    }

}
