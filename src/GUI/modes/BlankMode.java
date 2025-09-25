package GUI.modes;

import GUI.GUIConstants;
import GUI.MainSimulationWindow;
import GUI.PathChooser;
import GUI.ScreenTransform;
import GUI.shapesGUI.BlockGUI;
import GUI.shapesGUI.NodeGUI;
import GUI.shapesGUI.ShapeGUI;
import logic.communication.Scheduler;
import logic.communication.transmitters.Transmitter;
import logic.communication.transmitters.TransmitterType;
import logic.physics.Position;

import javax.swing.*;
import java.awt.*;

public class BlankMode extends Mode {

    private Position draggingPosition = null;
    private NodeGUI hoveredNode = null;

    public BlankMode(MainSimulationWindow mainWindow) { super(mainWindow); }

    @Override public void mouseClick(int x, int y) {
        draggingPosition = null;  // stop dragging
    }

    @Override
    public void mouseDrag(int x, int y) {
        if (draggingPosition == null)  // start dragging
            draggingPosition = mainWindow.getTransform().screenToWorld(x, y);
        mainWindow.setTransform(ScreenTransform.createFromRequirement(draggingPosition, new Point(x, y), mainWindow.getTransform().zoom()));
        mainWindow.getDrawingPanel().repaint();
    }

    @Override
    public void mouseRightClick(int x, int y) {
        ShapeGUI shape = mainWindow.getShapeAt(x, y);
        switch (shape) {
          case BlockGUI b -> showBlockPopup(b, x, y);
          case NodeGUI n -> showNodePopup(n, x, y);
          case null, default -> {
          }
        }

    }

    @Override
    public void mouseHover(int x, int y) {
        ShapeGUI shape = mainWindow.getShapeAt(x, y);
        if (shape instanceof NodeGUI n) {
            if (hoveredNode != null) hoveredNode.setHovered(false);
            hoveredNode = n;
            hoveredNode.setHovered(true);
        } else if (hoveredNode != null) {
            hoveredNode.setHovered(false);
            hoveredNode = null;
        } else {
            return;
        }
        mainWindow.getDrawingPanel().repaint();
    }

    @Override
    public void mouseWheelRotate(int clicks, int x, int y) {
        if (draggingPosition != null) return;   // do not allow zoom while dragging, it's a mess
        double zoom = mainWindow.getTransform().zoom() * Math.pow(GUIConstants.ZOOM_PER_WHEEL_CLICK, -clicks);
        Point mouseScreenLocation = new Point(x, y);
        Position mouseWorldPosition = mainWindow.getTransform().screenToWorld(mouseScreenLocation);
        mainWindow.setTransform(ScreenTransform.createFromRequirement(mouseWorldPosition, mouseScreenLocation, zoom));
        mainWindow.getDrawingPanel().repaint();
    }

    @Override
    public void close() {
        draggingPosition = null;
    }

    private void showNodePopup(NodeGUI node, int x, int y) {
        if (!mainWindow.isEditing()) return;
        JPopupMenu popup = new JPopupMenu();

        JMenuItem transmitterItem = new JMenuItem("Change Transmitter");
        transmitterItem.addActionListener(_ -> {
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
        scheduleItem.addActionListener(_ -> {
            Transmitter transmitter = node.node.getTransmitter();
            if (transmitter == null) {
                JOptionPane.showMessageDialog(mainWindow.getFrame(),
                        "Node does must have a transmitter assigned before scheduling.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String path = PathChooser.choosePath(GUIConstants.SCHEDULES_DIRECTORY);
            if (path == null) return;
            transmitter.clearSchedule();
            Scheduler.scheduleFromFile(transmitter, path);
        });

        JMenuItem removeItem = new JMenuItem("Remove");
        removeItem.addActionListener(_ -> mainWindow.removeNode(node));

        popup.add(transmitterItem);
        popup.add(scheduleItem);
        popup.add(removeItem);

        popup.show(mainWindow.getFrame(), x, y);
    }

    private void showBlockPopup(BlockGUI block, int x, int y) {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem removeItem = new JMenuItem("Remove");
        removeItem.addActionListener(_ -> mainWindow.removeBlock(block));
        popup.add(removeItem);
        popup.show(mainWindow.getFrame(), x, y);
    }
}
