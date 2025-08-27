package GUI;

import GUI.elevation.ElevationSlider;
import GUI.shapesGUI.ShapeGUI;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class DrawingPanel extends JPanel {

    private final MainSimulationWindow mainWindow;
    private Consumer<Graphics2D> previewDrawer = null;
    private final Color backgroundColor = ElevationSlider.getElevationColor(0.0);

    public DrawingPanel(MainSimulationWindow mainWindow) {
        super();
        this.mainWindow = mainWindow;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(backgroundColor);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (ShapeGUI shape : mainWindow.getShapes()) {
            shape.drawShape((Graphics2D) g, mainWindow.getTransform());
        }

        if (previewDrawer != null)
            previewDrawer.accept((Graphics2D) g);
    }

    public void setPreview(Consumer<Graphics2D> drawer) {
        this.previewDrawer = drawer;
        repaint();
    }

    public void clearPreview() {
        this.previewDrawer = null;
        repaint();
    }
}


