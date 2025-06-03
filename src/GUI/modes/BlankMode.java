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

        if (shape instanceof BlockGUI b) showBlockPopup(b, x, y);
        else if (shape instanceof NodeGUI n) showNodePopup(n, x, y);
    }

    private void showNodePopup(NodeGUI node, int x, int y) {
        JPopupMenu popup = new JPopupMenu();

        JMenuItem transmitterItem = new JMenuItem("Change Transmitter");
        transmitterItem.addActionListener(e -> {
            System.out.println("Transmitter change requested for node: " + node.getId());
            // TODO
        });

        JMenuItem scheduleItem = new JMenuItem("Schedule");
        scheduleItem.addActionListener(e -> {
            System.out.println("Schedule change requested for node: " + node.getId());
            // TODO
        });

        JMenuItem removeItem = new JMenuItem("Remove");
        removeItem.addActionListener(e -> {
            mainWindow.removeNode(node);
        });

        popup.add(transmitterItem);
        popup.add(scheduleItem);
        popup.add(removeItem);

        popup.show(mainWindow.getFrame(), x, y);
    }

    private void showBlockPopup(BlockGUI block, int x, int y) {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem removeItem = new JMenuItem("Remove");
        removeItem.addActionListener(e -> {
            mainWindow.removeBlock(block);
        });
        popup.add(removeItem);
        popup.show(mainWindow.getFrame(), x, y);
    }

}
