package GUI.modes;

import GUI.Constants;
import GUI.MainSimulationWindow;
import GUI.shapesGUI.NodeGUI;
import logic.physics.Position;

import java.awt.*;
import java.util.function.Consumer;

public class AddNodeMode extends Mode {
    public AddNodeMode(MainSimulationWindow mainWindow) { super(mainWindow); }

    private int nodeIdCounter = 0;

    @Override
    public void mouseClick(int x, int y) {
        Position pos = mainWindow.getTransform().screenToWorld(new Point(x, y));
        pos = new Position(pos.x, pos.y, mainWindow.getHeightAt(x, y) + 0.1);
        NodeGUI newNode = new NodeGUI(nodeIdCounter++, pos);
        System.out.println("Adding node at: " + newNode.getPosition());
        mainWindow.addNode(newNode);
    }

    @Override
    public void mouseHover(int x, int y) {
        Consumer<Graphics2D> previewDrawer = (g) -> drawPreview(g, x, y);
        mainWindow.getDrawingPanel().setPreview(previewDrawer);
    }

    private void drawPreview(Graphics2D g, int x, int y) {
        int size = Constants.NODE_RADIUS;
        g.setColor(Constants.PREVIEW_COLOR);
        g.setStroke(Constants.PREVIEW_STROKE);
        g.drawOval(x - size, y - size, 2 * size, 2 * size);
    }

    @Override
    public void close() {
        mainWindow.getDrawingPanel().clearPreview();
    }

}
