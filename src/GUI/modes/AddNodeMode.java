package GUI.modes;

import GUI.Constants;
import GUI.MainSimulationWindow;
import GUI.shapesGUI.NodeGUI;
import logic.shapes.Position;

import java.awt.*;
import java.util.function.Consumer;

public class AddNodeMode extends Mode {
    public AddNodeMode(MainSimulationWindow mainWindow) { super(mainWindow); }

    private int nodeIdCounter = 0;

    @Override
    public void mouseClick(int x, int y) {
        NodeGUI newNode = new NodeGUI(nodeIdCounter++, new Position(x, y, mainWindow.getHeight(x, y)));
        mainWindow.addNode(newNode);
    }

    @Override
    public void mouseHover(int x, int y) {
        Consumer<Graphics2D> previewDrawer = (g) -> drawPreview(g, x, y);
        mainWindow.getDrawingPanel().setPreview(previewDrawer);
    }

    private void drawPreview(Graphics2D g, int x, int y) {
        int size = Constants.NODE_RADIUS;
        g.setColor(Constants.NODE_PREVIEW_COLOR);
        g.drawOval(x - size, y - size, 2 * size, 2 * size);
    }

    @Override
    public void close() {
        mainWindow.getDrawingPanel().clearPreview();
    }

}
