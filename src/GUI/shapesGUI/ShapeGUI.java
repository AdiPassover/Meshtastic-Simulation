package GUI.shapesGUI;

import GUI.ScreenTransform;

import java.awt.*;

public interface ShapeGUI {
    boolean contains(int x, int y, ScreenTransform transform);
    void drawShape(Graphics2D g, ScreenTransform transform);
}
