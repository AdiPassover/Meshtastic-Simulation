package GUI.modes;

import GUI.MainSimulationWindow;

public class BlankMode extends Mode {

    public BlankMode(MainSimulationWindow mainWindow) { super(mainWindow); }

    @Override public void mouseClick(int x, int y) {}
    @Override public void mouseHover(int x, int y) {}
}
