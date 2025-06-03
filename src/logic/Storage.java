package logic;

import GUI.shapesGUI.BlockGUI;
import GUI.shapesGUI.NodeGUI;
import GUI.shapesGUI.ShapeGUI;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {

    public static final String DEFAULT_DIRECTORY = System.getProperty("user.dir")+File.separator+"src"+File.separator+"presets";

    public static void save(List<NodeGUI> nodes, List<BlockGUI> blocks) {
        File defaultFolder = new File(DEFAULT_DIRECTORY);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(defaultFolder);

        // Open a file chooser dialog for saving
        int status = fileChooser.showSaveDialog(null);
        if (status == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Check if the file already exists
            if (selectedFile.exists()) {
                int overwriteResult = JOptionPane.showConfirmDialog(null, "The file already exists. Do you want to overwrite it?", "Confirm Overwrite", JOptionPane.YES_NO_OPTION);
                if (overwriteResult != JOptionPane.YES_OPTION) { return; }
            }

            String filePath = selectedFile.getAbsolutePath();
            saveTo(nodes, blocks, filePath);
        }
    }

    private static void saveTo(List<NodeGUI> nodes, List<BlockGUI> blocks, String filePath) {
       try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(nodes);
            oos.writeObject(blocks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static List<ShapeGUI> load() {
        // Set the default folder for loading
        File defaultFolder = new File(DEFAULT_DIRECTORY);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(defaultFolder);

        // Open a file chooser dialog for loading
        int status = fileChooser.showOpenDialog(null);
        if (status == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.getAbsolutePath();
            return loadFrom(fileName);
        }
        return null;
    }

    private static List<ShapeGUI> loadFrom(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            List<NodeGUI> nodes = (List<NodeGUI>) ois.readObject();
            List<BlockGUI> blocks = (List<BlockGUI>) ois.readObject();

            List<ShapeGUI> shapes = new ArrayList<>();
            shapes.addAll(nodes);
            shapes.addAll(blocks);
            return shapes;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
