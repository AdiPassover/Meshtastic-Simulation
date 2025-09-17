package GUI.modes;

import GUI.MainSimulationWindow;

public abstract class Mode {

    protected final MainSimulationWindow mainWindow;

    public Mode(MainSimulationWindow mainWindow) { this.mainWindow = mainWindow; }

    public void mouseClick(int x, int y) {}
    public void mouseHover(int x, int y) {}
    public void mouseRightClick(int x, int y) {}
    public void mouseWheelRotate(int c, int x, int y) {}
    public void mouseDrag(int x, int y) {}

    public void open() {}
    public void close() {}

}
