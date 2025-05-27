package GUI.modes;

import GUI.MainSimulationWindow;

public abstract class Mode {

    protected MainSimulationWindow mainWindow;

    public Mode(MainSimulationWindow mainWindow) { this.mainWindow = mainWindow; }

    public abstract void mouseClick(int x, int y);
    public abstract void mouseHover(int x, int y);
    public void mouseRightClick(int x, int y) {}
    public void close() {}

}
