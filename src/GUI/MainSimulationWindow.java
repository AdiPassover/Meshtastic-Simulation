package GUI;

import GUI.modes.Mode;
import GUI.modes.ModeFactory;
import GUI.shapesGUI.BlockGUI;
import GUI.shapesGUI.NodeGUI;
import GUI.shapesGUI.ShapeGUI;

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
    private final JButton startButton,addNodeButton, addBlockButton, editButton, scheduleButton;

    private final ModeFactory modes = new ModeFactory(this);
    private Mode currentMode = modes.BLANK;

    List<NodeGUI> nodes = new ArrayList<>();
    List<BlockGUI> blocks = new ArrayList<>();


    public MainSimulationWindow() {
        frame = new JFrame("Simulation App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        frame.setLayout(new BorderLayout());

        drawingPanel = new DrawingPanel(this);
        drawingPanel.setBackground(Color.WHITE);
        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                currentMode.mouseClick(e.getX(), e.getY());
            }
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
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
        editButton = createModeChangeButton("Edit", modes.EDIT, SMALL_BUTTON_SIZE);
        scheduleButton = createButton("Schedule", e -> scheduleButton(), SMALL_BUTTON_SIZE);

        layoutComponents();
        frame.setVisible(true);
    }


    private void setCurrentMode(Mode mode) { currentMode.close(); currentMode = mode; }

    private void startButton() {
        // TODO
    }
    private void scheduleButton() {
        // TODO
    }

    public void addNode(NodeGUI node) {
        nodes.add(node);
        drawingPanel.repaint();
    }
    public void addBlock(BlockGUI block) {
        blocks.add(block);
        drawingPanel.repaint();
    }

    public Iterable<ShapeGUI> getShapes() {
        List<ShapeGUI> shapes = new ArrayList<>(blocks);
        shapes.addAll(nodes);
        return shapes;
    }

    public double getHeight(double x, double y) {
        double maxHeight = 0;
        for (BlockGUI b : blocks)
            if (b.contains((int)x, (int)y)) maxHeight = Math.max(maxHeight, b.getHeight());
        return maxHeight;
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
    private void layoutComponents() {
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
        controlPanel.add(editButton, gbc);

        gbc.gridx = 1;
        controlPanel.add(scheduleButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridwidth = 2;
        controlPanel.add(Box.createVerticalGlue(), gbc);

        frame.add(controlPanel, BorderLayout.EAST);
    }

    public DrawingPanel getDrawingPanel() {
        return drawingPanel;
    }

}
