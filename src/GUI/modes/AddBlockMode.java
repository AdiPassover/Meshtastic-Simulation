package GUI.modes;

import GUI.Constants;
import GUI.elevation.ElevationSlider;
import GUI.MainSimulationWindow;
import GUI.shapesGUI.BlockGUI;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class AddBlockMode extends Mode {
    public AddBlockMode(MainSimulationWindow mainWindow) { super(mainWindow); }

    private final List<Point> points = new ArrayList<>();
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
        if (points.size() < 3) return;

        isChoosingHeight = true;

        ElevationSlider.promptHeightSlider(height -> {
            int[][] pointsArray = new int[2][points.size()];
            for (int i = 0; i < points.size(); i++) {
                pointsArray[0][i] = points.get(i).x;
                pointsArray[1][i] = points.get(i).y;
            }

            Polygon polygon = new Polygon(pointsArray[0], pointsArray[1], points.size());
            mainWindow.addBlock(new BlockGUI(polygon, height));

            close();
        });
    }


    @Override
    public void mouseHover(int x, int y) {
        if (isChoosingHeight) return;
        mainWindow.getDrawingPanel().setPreview(g -> {
            drawPreview(g, x, y);
        });
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
        g.setColor(Constants.PREVIEW_COLOR);
        g.setStroke(Constants.PREVIEW_STROKE);
        g.drawPolygon(xs, ys, points.size() + 1);
    }

    @Override
    public void close() {
        isChoosingHeight = false;
        mainWindow.getDrawingPanel().clearPreview();
        points.clear();
    }


}
