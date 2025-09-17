package GUI.modes;

import GUI.GUIConstants;
import GUI.elevation.ElevationSlider;
import GUI.MainSimulationWindow;
import GUI.shapesGUI.BlockGUI;
import logic.physics.Position;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class AddBlockMode extends Mode {
    public AddBlockMode(MainSimulationWindow mainWindow) { super(mainWindow); }

    private final List<Point> points = new ArrayList<>();   // these are screen locations
    private boolean isChoosingHeight = false;

    @Override
    public void open() {
        points.clear();
        isChoosingHeight = false;
    }

    @Override
    public void mouseClick(int x, int y) {
        points.add(new Point(x, y));
    }

    @Override
    public void mouseRightClick(int x, int y) {
        points.add(new Point(x, y));    // add right click location to match preview
        if (points.size() < 3) {
            points.clear();  // cancel drawing
            return;
        }
        isChoosingHeight = true;

        ElevationSlider.promptHeightSlider(height -> {
            int[][] pointsArray = new int[2][points.size()];
            for (int i = 0; i < points.size(); i++) {
                // we saved the points as screen locations, so we need to convert to world position
                Position pos = mainWindow.getTransform().screenToWorld(points.get(i));
                pointsArray[0][i] = (int) pos.x;
                pointsArray[1][i] = (int) pos.y;
            }

            Polygon polygon = new Polygon(pointsArray[0], pointsArray[1], points.size());
            mainWindow.addBlock(new BlockGUI(polygon, height));

            close();
        });
    }

    @Override
    public void mouseHover(int x, int y) {
        if (isChoosingHeight) return;
        mainWindow.getDrawingPanel().setPreview(g -> drawPreview(g, x, y));
    }

    private void drawPreview(Graphics2D g, int x, int y) {
        if (points.isEmpty()) return;

        int[] xs = new int[points.size() + 1];
        int[] ys = new int[points.size() + 1];
        for (int i = 0; i < points.size(); i++) {
            xs[i] = points.get(i).x;
            ys[i] = points.get(i).y;
        }
        xs[points.size()] = x;
        ys[points.size()] = y;
        g.setColor(GUIConstants.PREVIEW_COLOR);
        g.setStroke(GUIConstants.PREVIEW_STROKE);
        g.drawPolygon(xs, ys, points.size() + 1);
    }

    @Override
    public void close() {
        isChoosingHeight = false;
        mainWindow.getDrawingPanel().clearPreview();
        points.clear();
    }
}
