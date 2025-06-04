package GUI;

import GUI.elevation.ElevationLegend;
import GUI.modes.Mode;
import GUI.modes.ModeFactory;
import GUI.shapesGUI.BlockGUI;
import GUI.shapesGUI.EdgeGUI;
import GUI.shapesGUI.NodeGUI;
import GUI.shapesGUI.ShapeGUI;
import logic.Storage;
import logic.communication.Ticker;
import logic.graph_objects.Graph;
import logic.physics.Block;
import logic.shapes.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;

public class MainSimulationWindow {

    private final JFrame frame;
    private final DrawingPanel drawingPanel;
    private final JPanel controlPanel;
    private final JButton startButton,addNodeButton, addBlockButton, saveButton, loadButton, nextButton, playButton,
            pauseButton, skipButton;

    private final ModeFactory modes = new ModeFactory(this);
    private Mode currentMode = modes.BLANK;
    private boolean isBuilding = true; // Used to track if we are in building mode
    private boolean isPlaying = false;

    private final List<NodeGUI> nodes = new ArrayList<>();
    private final List<EdgeGUI> edges = new ArrayList<>();
    private final List<BlockGUI> blocks = new ArrayList<>();

    private Ticker ticker;


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
        });
        controlPanel = new JPanel(new GridBagLayout());

        Dimension BIG_BUTTON_SIZE = new Dimension(220, 30);
        Dimension SMALL_BUTTON_SIZE = new Dimension(100, 30);
        startButton = createButton("Start", e -> startButton(), BIG_BUTTON_SIZE);
        addNodeButton = createModeChangeButton("Add Node", modes.ADD_NODE ,SMALL_BUTTON_SIZE);
        addBlockButton = createModeChangeButton("Add Block", modes.ADD_BLOCK, SMALL_BUTTON_SIZE);
        saveButton = createButton("Save", e -> saveButton(), SMALL_BUTTON_SIZE);
        loadButton = createButton("Load", e -> loadButton(), SMALL_BUTTON_SIZE);

        nextButton = createButton("Next", e -> nextButton(), SMALL_BUTTON_SIZE);
        playButton = createButton("Play", e -> playButton(), SMALL_BUTTON_SIZE);
        pauseButton = createButton("Pause", e -> pauseButton(), SMALL_BUTTON_SIZE);
        skipButton = createButton("Skip to End", e -> skipButton(), SMALL_BUTTON_SIZE);

        layoutBuildComponents();
        frame.setVisible(true);
    }


    private void setCurrentMode(Mode mode) { currentMode.close(); currentMode = mode; currentMode.open(); }
    public boolean isBuilding() { return isBuilding; }

    private void startButton() {
        isBuilding = !isBuilding;
        controlPanel.removeAll();

        if (isBuilding) {
            startButton.setText("Start");
            layoutBuildComponents();
            ticker = null;
        } else {
            startButton.setText("Stop");
            layoutSimulationComponents();

            List<Block> logicBlocks = new ArrayList<>();
            for (BlockGUI block : blocks) logicBlocks.add(block.block);
            ticker = new Ticker(getGraph(), logicBlocks);
        }

        controlPanel.revalidate();
        controlPanel.repaint();
    }

    private void saveButton() {
        String filePath = PathChooser.writePath(Constants.PRESETS_DIRECTORY);
        if (filePath != null) Storage.saveTo(nodes, blocks, filePath);
    }
    private void loadButton() {
        String filePath = PathChooser.choosePath(Constants.PRESETS_DIRECTORY);
        if (filePath == null) return;
        List<ShapeGUI> shapes = Storage.loadFrom(filePath);
        if (shapes != null) setShapes(shapes);
    }
    private void nextButton() {
        ticker.tick();
    }
    private void playButton() {
        isPlaying = true;
        ticker.getStatistics().printStats();
    }
    private void pauseButton() {
        isPlaying = false;
    }
    private void skipButton() {
        while (!ticker.isFinished()) {
            ticker.tick();
        }
    }




    public void addNode(NodeGUI node) {
        for (NodeGUI n : nodes)
            if (shouldAddEdge(node, n)) addEdge(node, n);

        nodes.add(node);
        drawingPanel.repaint();
    }
    public void addEdge(NodeGUI node1, NodeGUI node2) {
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
                if (!node1.node.hasNeighbour(node2.node) && shouldAddEdge(node1, node2)) {
                    addEdge(nodes.get(i), nodes.get(j));
                }
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
        drawingPanel.repaint();
    }

    public void setShapes(List<ShapeGUI> shapes) {
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
        this.nodes.addAll(nodes);
        this.blocks.addAll(blocks);

        for (int i = 0; i < nodes.size(); ++i) {
            NodeGUI node1 = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); ++j) {
                NodeGUI node2 = nodes.get(j);
                if (shouldAddEdge(node1, node2)) addEdge(node1, node2);
            }
        }
    }

    public Graph getGraph() {
        Graph graph = new Graph();
        for (NodeGUI node : nodes) graph.addNode(node.node);
        for (EdgeGUI edge : edges) graph.addEdge(edge.node1.node, edge.node2.node);
        return graph;
    }

    public DrawingPanel getDrawingPanel() { return drawingPanel; }
    public JFrame getFrame() { return frame; }

    public double getHeightAt(double x, double y) {
        double maxHeight = Constants.MINIMUM_HEIGHT - 1;
        for (BlockGUI b : blocks)
            if (b.contains((int)x, (int)y)) maxHeight = Math.max(maxHeight, b.getHeight());

        return maxHeight!=Constants.MINIMUM_HEIGHT-1 ? maxHeight : 0.0;
    }

    public ShapeGUI getShapeAt(int x, int y) {
        List<ShapeGUI> shapes = new ArrayList<>(nodes);
        shapes.addAll(blocks);

        for (ShapeGUI shape : shapes)
            if (shape.contains(x, y)) return shape;
        return null;
    }

    public boolean hasLineOfSight(Position pos1, Position pos2) {
        for (BlockGUI block : blocks)
            if (block.block.intersectsLine(pos1, pos2)) return false;
        return true;
    }

    private boolean shouldAddEdge(NodeGUI node1, NodeGUI node2) {
        return hasLineOfSight(node1.getPosition(), node2.getPosition());
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
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        JLabel elevationLabel = new JLabel("Elevation");
        elevationLabel.setFont(new Font("Arial", Font.BOLD, 17));
        elevationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        controlPanel.add(elevationLabel, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(2, 10, 5, 10);
        controlPanel.add(new ElevationLegend(new Dimension(100, 220)), gbc);

        frame.add(controlPanel, BorderLayout.EAST);
    }
    private void layoutSimulationComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        controlPanel.add(startButton, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        controlPanel.add(nextButton, gbc);

        gbc.gridx = 1;
        controlPanel.add(playButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        controlPanel.add(pauseButton, gbc);

        gbc.gridx = 1;
        controlPanel.add(skipButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel sliderLabel = new JLabel("Speed:");
        sliderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        controlPanel.add(sliderLabel, gbc);

        gbc.gridy++;
        int sliderMin = 1;
        int sliderMax = 20;
        double realMin = 0.1;
        double realMax = 2.0;

        JSlider speedSlider = new JSlider(sliderMin, sliderMax, 10); // initial value = 1.0

        speedSlider.addChangeListener(e -> {
            int sliderValue = speedSlider.getValue();
            double realValue = realMin + (realMax - realMin) * (sliderValue - sliderMin) / (sliderMax - sliderMin);
        });
        speedSlider.setMajorTickSpacing(10);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        controlPanel.add(speedSlider, gbc);
    }

    private void highlightButton(JButton button) {
        button.setBackground(Constants.CHOSEN_BUTTON_COLOR); // Light blue background
        button.setForeground(Color.BLACK);

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
        button.addActionListener(e -> {
            if (button.getBackground() == Constants.CHOSEN_BUTTON_COLOR) {
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

}
