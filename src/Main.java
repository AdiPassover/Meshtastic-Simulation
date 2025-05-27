import GUI.MainSimulationWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainSimulationWindow::new);
    }
}