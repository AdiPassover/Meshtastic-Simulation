package GUI.elevation;

import javax.swing.*;
import java.awt.*;
import java.util.Dictionary;
import java.util.Hashtable;

public class ElevationLegend extends JPanel {

    public ElevationLegend(Dimension dim) {
        setLayout(new BorderLayout());

        // Main elevation display (color bar)
        ElevationSlider elevationSlider = new ElevationSlider();
        elevationSlider.setPreferredSize(dim);  // Tall and narrow
        add(elevationSlider, BorderLayout.CENTER);

        // JSlider for labels only
        JSlider labelSlider = new JSlider(JSlider.VERTICAL, -100, 100, 0) {
            @Override
            protected void paintComponent(Graphics g) {
                // Only paint labels
                Dictionary<?, ?> labels = getLabelTable();
                if (labels != null) {
                    var keys = labels.keys();
                    while (keys.hasMoreElements()) {
                        var key = keys.nextElement();
                        JLabel label = (JLabel) labels.get(key);
                        int value = (Integer) key;
                        label.setBounds(0, 100-value, getWidth(), label.getPreferredSize().height);
                        label.paint(g.create(label.getX(), label.getY(), label.getWidth(), label.getHeight()));
                    }
                }
            }
        };
        labelSlider.setEnabled(false);
        labelSlider.setOpaque(false);
        labelSlider.setPaintTicks(false);
        labelSlider.setPaintTrack(false);
        labelSlider.setPaintLabels(true);
        labelSlider.setFocusable(false);

        // Setup label table
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        for (int i = -100; i <= 100; i += 25) {
            JLabel label = new JLabel("- " + i);
            label.setFont(new Font("Arial", Font.PLAIN, 15));
            labelTable.put(i, label);
        }
        labelSlider.setLabelTable(labelTable);

        // Add labels on the right
        add(labelSlider, BorderLayout.EAST);
    }
}

