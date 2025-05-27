package GUI;

import GUI.modes.Mode;
import GUI.modes.Modes;
import logic.graph_objects.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainSimulationWindow {

    private final JFrame frame;
    private final JPanel drawingPanel, controlPanel;
    private final JButton startButton,addNodeButton, addBlockButton, editButton, scheduleButton;

    private Mode currentMode = Modes.BLANK;



    public MainSimulationWindow() {
        frame = new JFrame("Simulation App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        frame.setLayout(new BorderLayout());

        drawingPanel = new JPanel();
        drawingPanel.setBackground(Color.WHITE);
        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                currentMode.mousePressed(e.getX(), e.getY());
            }
            @Override public void mouseMoved(MouseEvent e) {
                currentMode.mouseHover(e.getX(), e.getY());
            }
        });
        controlPanel = new JPanel(new GridBagLayout());

        Dimension BIG_BUTTON_SIZE = new Dimension(220, 30);
        Dimension SMALL_BUTTON_SIZE = new Dimension(100, 30);
        startButton = createButton("Start", e -> startButton(), BIG_BUTTON_SIZE);
        addNodeButton = createModeChangeButton("Add Node", Modes.ADD_NODE ,SMALL_BUTTON_SIZE);
        addBlockButton = createModeChangeButton("Add Block", Modes.ADD_BLOCK, SMALL_BUTTON_SIZE);
        editButton = createModeChangeButton("Edit", Modes.EDIT, SMALL_BUTTON_SIZE);
        scheduleButton = createButton("Schedule", e -> scheduleButton(), SMALL_BUTTON_SIZE);

        layoutComponents();
        frame.setVisible(true);
    }

    private void setCurrentMode(Mode mode) { currentMode = mode; }

    private void startButton() {
        // TODO
    }
    private void scheduleButton() {
        // TODO
    }


    private void highlightButton(JButton button) {
        button.setBackground(new Color(173, 216, 230)); // Light blue background
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
            setCurrentMode(mode);
            highlightButton(button);
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

}
