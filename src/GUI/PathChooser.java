package GUI;

import GUI.shapesGUI.BlockGUI;
import GUI.shapesGUI.NodeGUI;
import GUI.shapesGUI.ShapeGUI;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class PathChooser {

    /**
     * Opens a file chooser dialog where you can choose a path to save a file.
     * @return The absolute path of the selected file, or null if the user cancels the operation.
     */
    public static String writePath(String defaultDir) {
        File defaultFolder = new File(defaultDir);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(defaultFolder);

        // Open a file chooser dialog for saving
        int status = fileChooser.showSaveDialog(null);
        if (status == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Check if the file already exists
            if (selectedFile.exists()) {
                int overwriteResult = JOptionPane.showConfirmDialog(null, "The file already exists. Do you want to overwrite it?", "Confirm Overwrite", JOptionPane.YES_NO_OPTION);
                if (overwriteResult != JOptionPane.YES_OPTION) { return null; }
            }

            return selectedFile.getAbsolutePath();
        }
        return null;
    }

    /**
     * Opens a file chooser dialog where you can choose a path to load a file.
     * @return The absolute path of the selected file, or null if the user cancels the operation.
     */
    public static String choosePath(String defaultDir) {
        // Set the default folder for loading
        File defaultFolder = new File(defaultDir);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(defaultFolder);

        // Open a file chooser dialog for loading
        int status = fileChooser.showOpenDialog(null);
        if (status == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }
        return null;
    }

}
