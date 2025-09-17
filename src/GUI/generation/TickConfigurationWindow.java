package GUI.generation;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class TickConfigurationWindow extends JDialog {

    private final JCheckBox[] tickBoxes;
    private boolean[] result;

    public TickConfigurationWindow(JFrame parent, int numTicks, boolean[] initialTicks) {
        super(parent, "Configure Ticks", true);
        setLayout(new BorderLayout());

        JPanel tickPanel = new JPanel(new GridLayout(0, 10, 5, 5)); // 10 per row
        tickBoxes = new JCheckBox[numTicks];

        if (initialTicks == null || initialTicks.length != numTicks) {
            initialTicks = new boolean[numTicks];
            Arrays.fill(initialTicks, true);
        }
        for (int i = 0; i < numTicks; i++) {
            JCheckBox box = new JCheckBox(String.valueOf(i), initialTicks[i]); // default: checked
            tickBoxes[i] = box;
            tickPanel.add(box);
        }

        JScrollPane scrollPane = new JScrollPane(tickPanel);
        add(scrollPane, BorderLayout.CENTER);

        JButton doneButton = new JButton("Done");
        doneButton.addActionListener(e -> {
            result = new boolean[numTicks];
            for (int i = 0; i < numTicks; i++) {
                result[i] = tickBoxes[i].isSelected();
            }
            dispose();
        });

        add(doneButton, BorderLayout.SOUTH);

        setSize(500, 300);
        setLocationRelativeTo(parent);
    }

    public boolean[] getResult() {
        return result;
    }
}

