package GUI.modes;

import GUI.MainSimulationWindow;

public class EditMode extends Mode {

    public EditMode(MainSimulationWindow mainWindow) {
        super(mainWindow);
    }

    @Override
    public void mouseClick(int x, int y) {
        // Logic for handling mouse press in edit mode
        System.out.println("Mouse pressed at: " + x + ", " + y);
    }

    @Override
    public void mouseHover(int x, int y) {
        // Logic for handling mouse hover in edit mode
        System.out.println("Mouse hovering at: " + x + ", " + y);
    }
}
