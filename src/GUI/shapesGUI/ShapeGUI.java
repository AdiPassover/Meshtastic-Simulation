package GUI.shapesGUI;

import GUI.ScreenTransform;

import java.awt.*;

public interface ShapeGUI {

    boolean contains(int x, int y);

    void drawShape(Graphics2D g, ScreenTransform transform);

}
