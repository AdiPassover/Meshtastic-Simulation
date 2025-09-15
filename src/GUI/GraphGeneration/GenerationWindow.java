package GUI.GraphGeneration;

import GUI.shapesGUI.ShapeGUI;
import logic.communication.transmitters.TransmitterType;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GenerationWindow extends JDialog {

    public static final int DEFAULT_NUM_NODES = 40;
    public static final int DEFAULT_NUM_BLOCKS = 5;
    public static final int DEFAULT_NUM_MESSAGES = 1;
    public static final int DEFAULT_NUM_TICKS = 20;
    public static final int DEFAULT_SEED = 420;

    private final JTextField nodesField;
    private final JTextField blocksField;
    private final JTextField messagesField;
    private final JTextField numTicksField;
    private final JComboBox<String> transmitterTypeBox;
    private final JTextField seedField;

    private int rerollSeed = new Random().nextInt();

    private boolean[] isMessageTick = null;
    private List<ShapeGUI> generatedGraph;

    public GenerationWindow(JFrame parent) {
        super(parent, "Generate Simulation", true);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        formPanel.add(new JLabel("Number of nodes:"));
        nodesField = new JTextField(String.valueOf(DEFAULT_NUM_NODES));
        formPanel.add(nodesField);

        formPanel.add(new JLabel("Number of blocks:"));
        blocksField = new JTextField(String.valueOf(DEFAULT_NUM_BLOCKS));
        formPanel.add(blocksField);

        formPanel.add(new JLabel("Messages per tick:"));
        messagesField = new JTextField(String.valueOf(DEFAULT_NUM_MESSAGES));
        formPanel.add(messagesField);

        formPanel.add(new JLabel("Num messages ticks:"));
        numTicksField = new JTextField(String.valueOf(DEFAULT_NUM_TICKS));
        formPanel.add(numTicksField);

        formPanel.add(new JLabel("TransmitterType:"));
        String[] transmitterNames = TransmitterType.getTransmitterTypeNames();
        transmitterTypeBox = new JComboBox<>(transmitterNames);
        transmitterTypeBox.setSelectedIndex(0);
        formPanel.add(transmitterTypeBox);

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

        JButton configTicksButton = new JButton("Configure ticks");
        configTicksButton.addActionListener(e -> {
            int numTicks = Integer.parseInt(numTicksField.getText());
            TickConfigurationWindow tickWindow = new TickConfigurationWindow((JFrame) getParent(), numTicks, isMessageTick);
            tickWindow.setVisible(true);
            boolean[] configured = tickWindow.getResult();
            if (configured != null) {
                isMessageTick = configured;
            }
        });

        JButton doneButton = new JButton("Done");
        doneButton.addActionListener(e -> {
            // Example: Collect inputs here and call generator
            int numNodes = Integer.parseInt(nodesField.getText());
            int numBlocks = Integer.parseInt(blocksField.getText());
            int numMessages = Integer.parseInt(messagesField.getText());
            int numTicks = Integer.parseInt(numTicksField.getText());
            TransmitterType chosenType = TransmitterType.fromString((String) transmitterTypeBox.getSelectedItem());
            long seed = Long.parseLong(seedField.getText());

            boolean[] ticks;
            if (isMessageTick != null) {
                ticks = isMessageTick;
            } else {
                ticks = new boolean[numTicks];
                Arrays.fill(ticks, true);
            }

            GraphGenerator graphGenerator = new GraphGenerator(seed);
            generatedGraph = graphGenerator.generate(numNodes, numBlocks, numMessages, chosenType, ticks);
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


