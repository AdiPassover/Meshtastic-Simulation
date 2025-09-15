package GUI.shapesGUI;

import GUI.Constants;
import GUI.ScreenTransform;
import logic.communication.transmitters.TransmitterType;
import logic.graph_objects.Node;
import logic.physics.Position;

import java.awt.*;
import java.io.Serializable;

public class NodeGUI implements ShapeGUI, Serializable {

    public final Node node;
    private Color color = Constants.NODE_COLOR;

    public NodeGUI(int id, Position position) {
        node = new Node(id, position);
        setTransmitter(Constants.DEFAULT_TRANSMITTER_TYPE);
    }
    public NodeGUI(Node node) {
        this.node = node;
        if (node.getTransmitterType() != null)
            setColor(node.getTransmitterType().getColor());
        else
            setTransmitter(Constants.DEFAULT_TRANSMITTER_TYPE);
    }

    public Position getPosition() { return node.position; }
    public int getId() { return node.id; }
    public void setColor(Color color) { this.color = color; }

    public void setTransmitter(TransmitterType type) {
        node.setTransmitterByType(type);
        setColor(type.getColor());
    }

    @Override
    public boolean contains(int x, int y, ScreenTransform transform) {
        return node.position.distance2D(transform.screenToWorld(new Point(x, y))) <= Constants.NODE_RADIUS; // TODO: take zoom into account?
    }


    @Override
    public void drawShape(Graphics2D g, ScreenTransform transform) {
        int size = Constants.NODE_RADIUS;   // TODO: adjust by zoom?
        Point drawLoc = transform.worldToScreen(new Position(node.x(), node.y()));

        g.setColor(color);
        g.fillOval(drawLoc.x - size, drawLoc.y - size, 2 * size, 2 * size);

        g.setColor(Constants.NODE_OUTLINE_COLOR);
        g.setStroke(Constants.NODE_OUTLINE_STROKE);
        g.drawOval(drawLoc.x - size, drawLoc.y - size, 2 * size, 2 * size);

        g.setColor(Constants.NODE_TEXT_COLOR);
        g.setFont(Constants.NODE_FONT);

        g.drawString(Integer.toString(node.id), drawLoc.x - size / 4, drawLoc.y);
    }

}
