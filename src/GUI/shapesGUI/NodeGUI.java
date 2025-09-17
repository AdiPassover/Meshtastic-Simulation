package GUI.shapesGUI;

import GUI.GUIConstants;
import GUI.ScreenTransform;
import logic.communication.transmitters.TransmitterType;
import logic.graph_objects.Node;
import logic.physics.Position;

import java.awt.*;
import java.io.Serializable;

public class NodeGUI implements ShapeGUI, Serializable {

    public final Node node;
    private Color color = GUIConstants.NODE_COLOR;

    public NodeGUI(int id, Position position) {
        node = new Node(id, position);
        setTransmitter(GUIConstants.DEFAULT_TRANSMITTER_TYPE);
    }
    public NodeGUI(Node node) {
        this.node = node;
        if (node.getTransmitterType() != null)
            setColor(node.getTransmitterType().getColor());
        else
            setTransmitter(GUIConstants.DEFAULT_TRANSMITTER_TYPE);
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
        return transform.worldToScreen(node.position).distance(x, y) <= screenRadius(transform);
    }

    private static double screenRadius(ScreenTransform transform) {
        return GUIConstants.NODE_RADIUS * transform.zoom();
        // Point center = transform.worldToScreen(new Position(x, y));
        // Point edge = transform.worldToScreen(new Position(x + GUIConstants.NODE_RADIUS, y));
        // return center.distance(edge);
    }

    public static void drawPreview(Graphics2D g, int x, int y, ScreenTransform transform) {
        int size = (int) screenRadius(transform);
        g.setColor(GUIConstants.PREVIEW_COLOR);
        g.setStroke(GUIConstants.PREVIEW_STROKE);
        g.drawOval(x - size, y - size, 2 * size, 2 * size);
    }

    @Override
    public void drawShape(Graphics2D g, ScreenTransform transform) {
        int size = (int) screenRadius(transform);   // TODO: adjust by zoom? (A: I tried it, think it's better)
        Point drawLoc = transform.worldToScreen(new Position(node.x(), node.y()));

        g.setColor(color);
        g.fillOval(drawLoc.x - size, drawLoc.y - size, 2 * size, 2 * size);

        g.setColor(GUIConstants.NODE_OUTLINE_COLOR);
        g.setStroke(GUIConstants.NODE_OUTLINE_STROKE);
        g.drawOval(drawLoc.x - size, drawLoc.y - size, 2 * size, 2 * size);

        g.setColor(GUIConstants.NODE_TEXT_COLOR);
        Font font = new Font(GUIConstants.NODE_FONT_TYPEFACE, GUIConstants.NODE_FONT_STYLE, (int)(GUIConstants.NODE_FONT_SIZE * (size / (double) GUIConstants.NODE_RADIUS)));
        g.setFont(font);

        g.drawString(Integer.toString(node.id), drawLoc.x - size / 4, drawLoc.y);
    }

}
