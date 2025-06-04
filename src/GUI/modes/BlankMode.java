package GUI.modes;

import GUI.Constants;
import GUI.MainSimulationWindow;
import GUI.PathChooser;
import GUI.shapesGUI.BlockGUI;
import GUI.shapesGUI.NodeGUI;
import GUI.shapesGUI.ShapeGUI;
import logic.communication.Scheduler;
import logic.communication.transmitters.Transmitter;
import logic.communication.transmitters.TransmitterType;
import logic.communication.transmitters.TransmittersFactory;

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
        if (!mainWindow.isBuilding()) return;
        JPopupMenu popup = new JPopupMenu();

        JMenuItem transmitterItem = new JMenuItem("Change Transmitter");
        transmitterItem.addActionListener(e -> {
            String[] transmitterNames = TransmitterType.getTransmitterTypeNames();
            String chosenTypeStr = (String) JOptionPane.showInputDialog(
                    mainWindow.getFrame(),
                    "Choose a transmitter type:",
                    "Change Transmitter",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    transmitterNames,
                    transmitterNames[0]);
            if (chosenTypeStr == null) return; // User cancelled
            TransmitterType chosenType = TransmitterType.fromString(chosenTypeStr);
            node.setTransmitter(chosenType);
            mainWindow.getDrawingPanel().repaint();
        });

        JMenuItem scheduleItem = new JMenuItem("Schedule");
        scheduleItem.addActionListener(e -> {
            Transmitter transmitter = node.node.getTransmitter();
            if (transmitter == null) {
                JOptionPane.showMessageDialog(mainWindow.getFrame(),
                        "Node does must have a transmitter assigned before scheduling.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String path = PathChooser.choosePath(Constants.SCHEDULES_DIRECTORY);
            if (path == null) return;
            transmitter.clearSchedule();
            Scheduler.scheduleFromFile(transmitter, path);
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
