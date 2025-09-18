package GUI;

import GUI.generation.GenerationWindow;
import GUI.elevation.ElevationLegend;
import GUI.modes.Mode;
import GUI.modes.ModeFactory;
import GUI.shapesGUI.BlockGUI;
import GUI.shapesGUI.EdgeGUI;
import GUI.shapesGUI.NodeGUI;
import GUI.shapesGUI.ShapeGUI;
import logic.Statistics;
import logic.Storage;
import logic.communication.Message;
import logic.communication.TickerBatch;
import logic.graph_objects.Graph;
import logic.graph_objects.Node;
import logic.physics.Block;
import logic.physics.PhysicsEngine;
import logic.physics.Position;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class MainSimulationWindow {

    private final JFrame frame;
    private final DrawingPanel drawingPanel;
    private final JPanel controlPanel, transformPanel;
    private final JLabel[] transformLabels = { createTransformLabel("X: 0"), createTransformLabel("Y: 0"), createTransformLabel("Zoom: 1.0x") };
    private final JButton startButton,addNodeButton, addBlockButton, saveButton, loadButton, nextButton, playButton,
            pauseButton, skipButton, generateButton;
    private final JPanel statsPanel = new JPanel();
    private final JPanel receivedPanel = new JPanel();
    private final JSpinner batchSizeField = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

    private final ModeFactory modes = new ModeFactory(this);
    private Mode currentMode = modes.BLANK;
    private boolean isBuilding = true; // Used to track if we are in building mode
    private boolean isPlaying = false;

    private TickerBatch tickers;
    private final PhysicsEngine physics = new PhysicsEngine();

    private double currentDelay = 1.0; // Delay for the simulation, in seconds
    private Timer playTimer;

    private final List<NodeGUI> nodes = new ArrayList<>();
    private final List<EdgeGUI> edges = new ArrayList<>();
    private final List<BlockGUI> blocks = new ArrayList<>();

    private ScreenTransform transform = new ScreenTransform(0, 0, 1);


    public MainSimulationWindow() {
        frame = new JFrame("Meshtastic Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        frame.setLayout(new BorderLayout());

        drawingPanel = new DrawingPanel(this);
        drawingPanel.setBackground(Color.WHITE);
        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    drawingPanel.requestFocusInWindow();
                    currentMode.mouseClick(e.getX(), e.getY());
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    currentMode.mouseRightClick(e.getX(), e.getY());
                }
            }
        });
        drawingPanel.addMouseMotionListener(new MouseAdapter() {
            @Override public void mouseMoved(MouseEvent e) {
                currentMode.mouseHover(e.getX(), e.getY());
            }
            @Override public void mouseDragged(MouseEvent e) {
                currentMode.mouseDrag(e.getX(), e.getY());
            }
        });
        drawingPanel.addKeyListener(new KeyListener() {
            @Override public void keyTyped(KeyEvent e) {}
            @Override public void keyReleased(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { // Deselect current mode on ESC
                    setCurrentMode(modes.BLANK);
                    highlightButton(null);
                }
            }
        });
        drawingPanel.addMouseWheelListener(e -> currentMode.mouseWheelRotate(e.getWheelRotation(), e.getLocationOnScreen().x, e.getLocationOnScreen().y));

        drawingPanel.setFocusable(true);

        controlPanel = new JPanel(new GridBagLayout());

        transformPanel = new JPanel(new FlowLayout());
        transformPanel.setLayout(new GridLayout(3, 1, 5, 5)); // 3 rows, spacing between
        transformPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                "Transform",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 16),
                Color.BLACK
        ));
        transformPanel.setBackground(new Color(240, 240, 240));
        for (JLabel label : transformLabels) {
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            transformPanel.add(label);
        }

        Dimension BIG_BUTTON_SIZE = new Dimension(220, 30);
        Dimension SMALL_BUTTON_SIZE = new Dimension(100, 30);

        startButton = createButton("Start", _ -> startButton(), BIG_BUTTON_SIZE);
        generateButton = createButton("Generate", _ -> generateButton(), BIG_BUTTON_SIZE);

        addNodeButton = createModeChangeButton("Add Node", modes.ADD_NODE ,SMALL_BUTTON_SIZE);
        addBlockButton = createModeChangeButton("Add Block", modes.ADD_BLOCK, SMALL_BUTTON_SIZE);

        saveButton = createButton("Save", _ -> saveButton(), SMALL_BUTTON_SIZE);
        loadButton = createButton("Load", _ -> loadButton(), SMALL_BUTTON_SIZE);

        nextButton = createButton("Next", _ -> nextButton(), SMALL_BUTTON_SIZE);
        playButton = createButton("Play", _ -> playButton(), SMALL_BUTTON_SIZE);
        pauseButton = createButton("Pause", _ -> pauseButton(), SMALL_BUTTON_SIZE);
        skipButton = createButton("Skip to End", _ -> skipButton(), SMALL_BUTTON_SIZE);

        layoutBuildComponents();
        frame.setVisible(true);

        playTimer = new Timer(0, _ -> {
            if (!tickers.isFinished()) {
                tick(true);
            } else {
                playTimer.stop();
                isPlaying = false;
            }
        });
    }


    private void setCurrentMode(Mode mode) { currentMode.close(); currentMode = mode; currentMode.open(); }
    public boolean isBuilding() { return isBuilding; }

    private static JLabel createTransformLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        label.setPreferredSize(new Dimension(100, 25)); // fixed size
        label.setMinimumSize(new Dimension(100, 25));
        label.setMaximumSize(new Dimension(100, 25));
        return label;
    }

    private void startButton() {
        isBuilding = !isBuilding;
        controlPanel.removeAll();

        if (isBuilding) {
            startButton.setText("Start");
            layoutBuildComponents();
            playTimer.stop();
            tickers = null;
        } else {
            startButton.setText("Stop");
            batchSizeField.setEnabled(true);
            layoutSimulationComponents();
        }

        controlPanel.revalidate();
        controlPanel.repaint();
    }

    private void generateButton() {
        GenerationWindow genWindow = new GenerationWindow(frame);
        setShapes(genWindow.getGeneratedGraph());
    }

    private void saveButton() {
        String filePath = PathChooser.writePath(GUIConstants.PRESETS_DIRECTORY);
        if (filePath != null) Storage.saveTo(nodes, blocks, filePath);
    }
    private void loadButton() {
        String filePath = PathChooser.choosePath(GUIConstants.PRESETS_DIRECTORY);
        if (filePath == null) return;
        List<ShapeGUI> shapes = Storage.loadFrom(filePath);
        setShapes(shapes);
    }
    private void nextButton() {
        tick(true);
    }
    private void skipButton() {
        while (!tickers.isFinished()) {
            tick(false);
        }
        updateStats();
    }
    private void playButton() {
        if (!isPlaying) {
            isPlaying = true;
            setTimerDelay();
            playTimer.setInitialDelay(0);
            playTimer.start();
        }
    }
    private void pauseButton() {
        if (isPlaying) {
            isPlaying = false;
            playTimer.stop();
        }
    }
    private void tick(boolean updateGUI) {
        if (tickers == null) {
            List<Block> logicBlocks = blocks.stream().map(BlockGUI::getBlock).toList();
            batchSizeField.setEnabled(false);
            tickers = new TickerBatch((int) batchSizeField.getValue(), getGraph(), logicBlocks);
        }
        tickers.tick();
        if (updateGUI) updateStats();
    }

    private void setTimerDelay() {
        int delayInMillis = (int)(currentDelay * 1000); // Set the current delay in milliseconds
        playTimer.setDelay(delayInMillis);
    }

    public void addNode(NodeGUI node) {
        for (NodeGUI n : nodes)
            if (shouldAddEdge(node, n)) addEdge(node, n);

        nodes.add(node);
        drawingPanel.repaint();
    }
    private void addEdge(NodeGUI node1, NodeGUI node2) {
        EdgeGUI edge = new EdgeGUI(node1, node2);
        edges.add(edge);
        drawingPanel.repaint();
    }
    public void addBlock(BlockGUI block) {
        for (int i = 0; i < edges.size(); ++i) {
            EdgeGUI e = edges.get(i);
            Position pos1 = e.node1.getPosition(), pos2 = e.node2.getPosition();
            if (block.block.intersectsLine(pos1, pos2)) {
                edges.remove(i--);
            }
        }

        physics.addBlock(block.block);
        blocks.add(block);
        drawingPanel.repaint();
    }
    public void removeNode(NodeGUI node) {
        nodes.remove(node);
        edges.removeIf(edge -> edge.node1.equals(node) || edge.node2.equals(node));
        drawingPanel.repaint();
    }
    public void removeBlock(BlockGUI block) {
        blocks.remove(block);

        for (int i = 0; i < nodes.size(); ++i) {
            NodeGUI node1 = nodes.get(i);
            for (int j = i+1; j < nodes.size(); ++j) {
                NodeGUI node2 = nodes.get(j);
                if (shouldAddEdge(node1, node2))
                    addEdge(nodes.get(i), nodes.get(j));
            }
        }

        drawingPanel.repaint();
    }

    public Iterable<ShapeGUI> getShapes() {
        List<ShapeGUI> shapes = new ArrayList<>(blocks);
        shapes.addAll(edges);
        shapes.addAll(nodes);
        return shapes;
    }

    public void clear() {
        nodes.clear();
        edges.clear();
        blocks.clear();
        physics.clearBlocks();
        drawingPanel.repaint();
    }

    public void setShapes(List<ShapeGUI> shapes) {
        if (shapes == null) return;

        List<NodeGUI> nodes = new ArrayList<>();
        List<BlockGUI> blocks = new ArrayList<>();
        for (ShapeGUI shape : shapes) {
            if (shape instanceof NodeGUI node) nodes.add(node);
            else if (shape instanceof BlockGUI block) blocks.add(block);
        }
        setShapes(nodes, blocks);
    }

    public void setShapes(List<NodeGUI> nodes, List<BlockGUI> blocks) {
        clear();
        for (BlockGUI block : blocks) addBlock(block);
        for (NodeGUI node : nodes) addNode(node);
        drawingPanel.repaint();
    }

    public Graph getGraph() {
        Graph graph = new Graph();
        for (NodeGUI node : nodes) graph.addNode(node.node);
        for (EdgeGUI edge : edges) graph.addEdge(edge.node1.node, edge.node2.node);
        return graph;
    }

    public DrawingPanel getDrawingPanel() { return drawingPanel; }
    public JFrame getFrame() { return frame; }

    public double getHeightAt(int x, int y) {
        Position pos = getTransform().screenToWorld(x, y);
        return physics.getHeightAt(pos.x, pos.y);
    }

    public ShapeGUI getShapeAt(int x, int y) {

        List<ShapeGUI> shapes = new ArrayList<>(nodes);
        shapes.addAll(blocks);

        for (ShapeGUI shape : shapes)
            if (shape.contains(x, y, getTransform())) return shape;
        return null;
    }

    private boolean shouldAddEdge(NodeGUI node1, NodeGUI node2) {  // check physics, and they are not already connected
        return physics.shouldAddEdge(node1.node, node2.node) && !node1.node.hasNeighbour(node2.node);
    }

    private void layoutBuildComponents() {
        frame.add(drawingPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        controlPanel.add(startButton, gbc);

        gbc.gridy++;
        controlPanel.add(generateButton, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        controlPanel.add(addNodeButton, gbc);

        gbc.gridx = 1;
        controlPanel.add(addBlockButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        controlPanel.add(saveButton, gbc);

        gbc.gridx = 1;
        controlPanel.add(loadButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridwidth = 2;
        controlPanel.add(Box.createVerticalGlue(), gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        controlPanel.add(transformPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridwidth = 2;
        controlPanel.add(Box.createVerticalGlue(), gbc);

        gbc.gridy++;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        JLabel elevationLabel = new JLabel("Elevation");
        elevationLabel.setFont(new Font("Arial", Font.BOLD, 17));
        elevationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        controlPanel.add(elevationLabel, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(2, 10, 5, 10);
        controlPanel.add(new ElevationLegend(new Dimension(100, 220)), gbc);

        statsPanel.removeAll();

        frame.add(controlPanel, BorderLayout.EAST);
    }
    private void layoutSimulationComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;

        gbc.gridwidth = 1;
        JLabel batchSizeLabel = new JLabel("Batch size:");
        gbc.gridx = 0;
        controlPanel.add(batchSizeLabel, gbc);

        gbc.gridx = 1;
        controlPanel.add(batchSizeField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        controlPanel.add(startButton, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        controlPanel.add(nextButton, gbc);

        gbc.gridx = 1;
        controlPanel.add(skipButton, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        controlPanel.add(playButton, gbc);

        gbc.gridx = 1;
        controlPanel.add(pauseButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel sliderLabel = new JLabel("Delay:");
        sliderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        controlPanel.add(sliderLabel, gbc);

        gbc.gridy++;
        int sliderMin = 0;
        int sliderMax = 20;
        double realMin = 0.0;
        double realMax = 2.0;

        JSlider speedSlider = new JSlider(sliderMin, sliderMax, 10); // initial value = 1.0
        currentDelay = 1.0;
        speedSlider.addChangeListener(_ -> {
            int sliderValue = speedSlider.getValue();
            currentDelay = realMin + (realMax - realMin) * (sliderValue - sliderMin) / (sliderMax - sliderMin);
            if (isPlaying) {
                setTimerDelay();
            }
        });
        speedSlider.setMajorTickSpacing(5);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        controlPanel.add(speedSlider, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        statsPanel.setLayout(new GridLayout(6, 1, 5, 5)); // 6 rows, spacing between items
        statsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                "Simulation Statistics",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 16),
                Color.BLACK
        ));
        statsPanel.setBackground(new Color(230, 230, 230)); // Light gray

        Font statFont = new Font("SansSerif", Font.PLAIN, 14);

        JLabel currentTickLabel = new JLabel("Current Tick: 0");
        currentTickLabel.setFont(statFont);
        JLabel transmissionsLabel = new JLabel("Transmissions: 0");
        transmissionsLabel.setFont(statFont);
        JLabel originalMessagesLabel = new JLabel("Original Messages: 0");
        originalMessagesLabel.setFont(statFont);
        JLabel successfulMessagesLabel = new JLabel("Successful Messages: 0");
        successfulMessagesLabel.setFont(statFont);
        JLabel averageLatencyLabel = new JLabel("Average Latency: 0.00");
        averageLatencyLabel.setFont(statFont);
        JLabel collisionsLabel = new JLabel("Collisions: 0");
        collisionsLabel.setFont(statFont);

        statsPanel.add(currentTickLabel);
        statsPanel.add(transmissionsLabel);
        statsPanel.add(originalMessagesLabel);
        statsPanel.add(successfulMessagesLabel);
        statsPanel.add(averageLatencyLabel);
        statsPanel.add(collisionsLabel);

        controlPanel.add(statsPanel, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        receivedPanel.setLayout(new GridLayout(1, 1, 5, 5)); // 6 rows, spacing between items
        receivedPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                "Received Messages",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 16),
                Color.BLACK
        ));
        receivedPanel.setBackground(new Color(230, 230, 230)); // Light gray
        receivedPanel.removeAll();

        controlPanel.add(receivedPanel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        controlPanel.add(transformPanel, gbc);
    }

    private void updateStats() {
        if (tickers == null) return;

        Statistics.AverageStatistics stats = tickers.getStatistics();
        for (Component comp : statsPanel.getComponents()) {
            if (comp instanceof JLabel label) {
                switch (label.getText().split(":")[0]) {
                    case "Transmissions" -> label.setText("Transmissions: " + stats.getTotalTransmissions());
                    case "Original Messages" -> label.setText("Original Messages: " + stats.getOriginalMessages());
                    case "Successful Messages" -> label.setText("Successful Messages: " + stats.getSuccessfulMessages());
                    case "Average Latency" -> label.setText(String.format("Average Latency: %.2f", stats.getAverageLatency()));
                    case "Collisions" -> label.setText("Collisions: " + stats.getNumCollisions());
                    case "Current Tick" -> label.setText("Current Tick: " + tickers.getCurrentTick());
                }
            }
        }

        Map<Message, Node> messagesReceived = tickers.getMessagesReceivedThisTick();
        receivedPanel.removeAll();
        receivedPanel.setLayout(new GridLayout(Math.min(messagesReceived.size(), GUIConstants.MAX_RCVED_MESSAGES_DISPLAYED),
                           1, 5, 5)); // 6 rows, spacing between items
        receivedPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                "Received Messages",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 16),
                Color.BLACK
        ));
        receivedPanel.setBackground(new Color(230, 230, 230)); // Light gray
        if (messagesReceived.isEmpty()) {
            JLabel noMessagesLabel = new JLabel("No messages received this tick");
            noMessagesLabel.setHorizontalAlignment(SwingConstants.CENTER);
            receivedPanel.add(noMessagesLabel);
        } else {
            int count = 0;
            for (Map.Entry<Message, Node> entry : messagesReceived.entrySet()) {
                Message msg = entry.getKey();
                Node sourceNode = entry.getValue();
                JLabel messageLabel = new JLabel(sourceNode.id + " received: " + msg.payload +
                        " from " + msg.sourceId);
                messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                receivedPanel.add(messageLabel);
                if (++count >= GUIConstants.MAX_RCVED_MESSAGES_DISPLAYED) break;
            }
        }
    }

    private void highlightButton(JButton button) {
        if (button != null) {
            button.setBackground(GUIConstants.CHOSEN_BUTTON_COLOR); // Light blue background
            button.setForeground(Color.BLACK);
        }

        for (Component comp : controlPanel.getComponents()) {
            if (comp instanceof JButton otherButton && comp != button) {
                otherButton.setBackground(null);
                otherButton.setForeground(null);
            }
        }
    }
    private JButton createButton(String text, ActionListener listener, Dimension size) {
        JButton button = new JButton(text);
        button.setPreferredSize(size);
        button.addActionListener(listener);
        return button;
    }
    private JButton createModeChangeButton(String text, Mode mode, Dimension size) {
        JButton button = new JButton(text);
        button.setPreferredSize(size);
        button.addActionListener(_ -> {
            if (button.getBackground() == GUIConstants.CHOSEN_BUTTON_COLOR) {
                setCurrentMode(modes.BLANK);
                button.setBackground(null);
                button.setForeground(null);
            } else {
                setCurrentMode(mode);
                highlightButton(button);
            }

        });
        return button;
    }

    public ScreenTransform getTransform() {
        return transform;
    }

    public void setTransform(ScreenTransform t) {
        transform = t;
        updateTransformLabels();
    }

    private void updateTransformLabels() {
        transformLabels[0].setText(String.format("X: %.2f", transform.x()));
        transformLabels[1].setText(String.format("Y: %.2f", transform.y()));
        transformLabels[2].setText(String.format("Zoom: %.4f", transform.zoom()));
    }

}
