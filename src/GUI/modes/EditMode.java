package GUI.modes;

public class EditMode extends Mode {

    @Override
    public void mousePressed(int x, int y) {
        // Logic for handling mouse press in edit mode
        System.out.println("Mouse pressed at: " + x + ", " + y);
    }

    @Override
    public void mouseHover(int x, int y) {
        // Logic for handling mouse hover in edit mode
        System.out.println("Mouse hovering at: " + x + ", " + y);
    }
}
