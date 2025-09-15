package GUI;

import GUI.shapesGUI.ShapeGUI;
import logic.communication.transmitters.TransmitterType;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class GenerationWindow extends JDialog {

    public static final int DEFAULT_NUM_NODES = 40;
    public static final int DEFAULT_NUM_BLOCKS = 5;
    public static final int DEFAULT_NUM_MESSAGES = 1;
    public static final int DEFAULT_FINAL_TICK = 20;
    public static final int DEFAULT_SEED = 420;

    private final JTextField nodesField;
    private final JTextField blocksField;
    private final JTextField messagesField;
    private final JTextField finalTickField;
    private final JComboBox<String> transmitterTypeBox;
    private final JTextField seedField;

    private int rerollSeed = new Random().nextInt();

    private List<ShapeGUI> generatedGraph;

    public GenerationWindow(JFrame parent) {
        super(parent, "Generate Simulation", true);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        // Nodes
        formPanel.add(new JLabel("Number of nodes:"));
        nodesField = new JTextField(String.valueOf(DEFAULT_NUM_NODES));
        formPanel.add(nodesField);

        // Blocks
        formPanel.add(new JLabel("Number of blocks:"));
        blocksField = new JTextField(String.valueOf(DEFAULT_NUM_BLOCKS));
        formPanel.add(blocksField);

        // Messages per tick
        formPanel.add(new JLabel("Messages per tick:"));
        messagesField = new JTextField(String.valueOf(DEFAULT_NUM_MESSAGES));
        formPanel.add(messagesField);

        // Final message tick
        formPanel.add(new JLabel("Final message tick:"));
        finalTickField = new JTextField(String.valueOf(DEFAULT_FINAL_TICK));
        formPanel.add(finalTickField);

        // Protocol dropdown
        formPanel.add(new JLabel("TransmitterType:"));
        String[] transmitterNames = TransmitterType.getTransmitterTypeNames();
        transmitterTypeBox = new JComboBox<>(transmitterNames);
        transmitterTypeBox.setSelectedIndex(0);
        formPanel.add(transmitterTypeBox);

        // Seed with reroll
        formPanel.add(new JLabel("Seed:"));
        JPanel seedPanel = new JPanel(new BorderLayout());
        seedField = new JTextField(String.valueOf(DEFAULT_SEED));
        JButton rerollButton = new JButton("Reroll");
        rerollButton.addActionListener(e -> {
            rerollSeed = new Random().nextInt();
            seedField.setText(String.valueOf(rerollSeed));
        });
        seedPanel.add(seedField, BorderLayout.CENTER);
        seedPanel.add(rerollButton, BorderLayout.EAST);
        formPanel.add(seedPanel);

        // Configure ticks button
        JButton configTicksButton = new JButton("Configure ticks");
        configTicksButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Not implemented", //TODO: Add tick configuration window
                    "Configure Ticks",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        // Done button
        JButton doneButton = new JButton("Done");
        doneButton.addActionListener(e -> {
            // Example: Collect inputs here and call generator
            int numNodes = Integer.parseInt(nodesField.getText());
            int numBlocks = Integer.parseInt(blocksField.getText());
            int numMessages = Integer.parseInt(messagesField.getText());
            int finalTick = Integer.parseInt(finalTickField.getText());
            TransmitterType chosenType = TransmitterType.fromString((String) transmitterTypeBox.getSelectedItem());
            long seed = Long.parseLong(seedField.getText());

            System.out.println("Generating with params:");
            System.out.println("Nodes=" + numNodes + ", Blocks=" + numBlocks +
                    ", Msg/tick=" + numMessages + ", FinalTick=" + finalTick +
                    ", Protocol=" + chosenType +
                    ", Seed=" + seed);

            GraphGenerator graphGenerator = new GraphGenerator(seed);
            boolean[] isMessageTick = new boolean[finalTick + 1];
            for (int i = 0; i < isMessageTick.length; ++i) isMessageTick[i] = true; // TODO: Change based on tick configuration
            generatedGraph = graphGenerator.generate(numNodes, numBlocks, numMessages, chosenType, isMessageTick); // TODO add tick configuration and change null here
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(configTicksButton);
        buttonPanel.add(doneButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }


    public List<ShapeGUI> getGeneratedGraph() {
        return generatedGraph;
    }


}


