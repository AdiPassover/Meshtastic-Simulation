package GUI.modes;

import GUI.MainSimulationWindow;
import GUI.shapesGUI.BlockGUI;
import GUI.shapesGUI.NodeGUI;
import GUI.shapesGUI.ShapeGUI;

import javax.swing.*;

public class BlankMode extends Mode {

    public BlankMode(MainSimulationWindow mainWindow) { super(mainWindow); }

    @Override public void mouseClick(int x, int y) {}
    @Override public void mouseHover(int x, int y) {}

    @Override
    public void mouseRightClick(int x, int y) {
        ShapeGUI shape = mainWindow.getShapeAt(x, y);
        if (shape == null) return;

        if (shape instanceof BlockGUI b) blockOptions();
        else if (shape instanceof NodeGUI n) {
            System.out.println("Node right-clicked: " + n.getId());
            showNodePopup(n, x, y);
        }
    }

    private void blockOptions() {

    }

    private void nodeOptions() {

    }

    private void showNodePopup(NodeGUI node, int x, int y) {
        JPopupMenu popup = new JPopupMenu();

        JMenuItem editItem = new JMenuItem("Edit");
        editItem.addActionListener(e -> {
            System.out.println("Edit node: " + node.getId());
        });

        JMenuItem removeItem = new JMenuItem("Remove");
        removeItem.addActionListener(e -> {
            mainWindow.removeNode(node);
        });

        popup.add(editItem);
        popup.add(removeItem);

        popup.show(mainWindow.getFrame(), x, y);
    }

}
