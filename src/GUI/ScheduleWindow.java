package GUI;

import logic.communication.Message;
import logic.graph_objects.Node;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Set;

public class ScheduleWindow {

    public static void viewSchedule(List<Node> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No nodes available.");
            return;
        }

        // Find the maximum tick where any message is scheduled
        int maxTick = 0;
        for (Node node : nodes) {
            for (Message m : node.getTransmitter().getAllOriginalScheduledMessages())
                maxTick = Math.max(maxTick, m.creationTick);
        }

        // Column names: "Tick", then node IDs
        String[] columnNames = new String[nodes.size() + 2];
        columnNames[0] = "Tick";
        columnNames[1] = "Num Msgs";
        for (int i = 0; i < nodes.size(); i++) {
            columnNames[i + 2] = "Node " + nodes.get(i).id;
        }

        // Build the table model
        DefaultTableModel model = new DefaultTableModel(columnNames, maxTick + 1);

        for (int tick = 0; tick <= maxTick; tick++) {
            model.setValueAt(tick, tick, 0); // First column = tick
            int count = 0;
            for (int col = 0; col < nodes.size(); col++) {
                int currCol = col + 2;
                Node node = nodes.get(col);
                Message m = node.getTransmitter().messageScheduledAt(tick);
                if (m != null) {
                    model.setValueAt("→ " + m.destinationId, tick, currCol);
                    ++count;
                } else {
                    model.setValueAt("", tick, currCol);
                }
            }
            model.setValueAt(count, tick, 1); // Second column = number of messages
        }

        // Create table
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Show in scrollable window
        JFrame frame = new JFrame("Schedule Viewer");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new JScrollPane(table));
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}
