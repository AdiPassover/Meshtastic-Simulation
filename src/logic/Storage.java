package logic;

import GUI.shapesGUI.BlockGUI;
import GUI.shapesGUI.NodeGUI;
import GUI.shapesGUI.ShapeGUI;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {

    public static void saveTo(List<NodeGUI> nodes, List<BlockGUI> blocks, String filePath) {
       try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(nodes);
            oos.writeObject(blocks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<ShapeGUI> loadFrom(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            List<NodeGUI> nodes = (List<NodeGUI>) ois.readObject();
            List<BlockGUI> blocks = (List<BlockGUI>) ois.readObject();

            List<ShapeGUI> shapes = new ArrayList<>();
            shapes.addAll(nodes);
            shapes.addAll(blocks);
            return shapes;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
