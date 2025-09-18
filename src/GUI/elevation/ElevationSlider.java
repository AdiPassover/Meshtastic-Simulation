package GUI.elevation;

import GUI.GUIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class ElevationSlider extends JPanel {
    private int selectedY;
    private double selectedHeight;
    private final Consumer<Double> onSelect;

    private final JLabel infoLabel;

    public ElevationSlider() {
        this.onSelect = this::setHeight;
        this.infoLabel = null; // No info label by default
        setPreferredSize(new Dimension(50, 300));
        setHeight(0); // default selection at 0
    }

    public ElevationSlider(Consumer<Double> onSelect, JLabel infoLabel) {
        this.onSelect = onSelect;
        this.infoLabel = infoLabel;
        setPreferredSize(new Dimension(50, 300));
        setHeight(0); // default selection at 0

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectedY = e.getY();
                selectedHeight = pixelToHeight(selectedY);
                onSelect.accept(selectedHeight);
                updateInfoLabel();
                repaint();
            }
        });
    }

    private void setHeight(double height) {
        selectedHeight = height;
        selectedY = heightToPixel(height);
        updateInfoLabel();
    }

    private void updateInfoLabel() {
        if (infoLabel != null)
            infoLabel.setText(String.format("Height: %.2f", selectedHeight));
    }

    private double pixelToHeight(int y) {
        float heightRange = 200f; // [-100, 100]
        return 100 - ((float) y / getHeight()) * heightRange;
    }

    private int heightToPixel(double height) {
        return (int) ((100 - height) / 200.0 * getHeight());
    }

    private static double normalize(double value, double min, double max) {
        return (value - min) / (max - min);
    }


    public static Color getElevationColor(double height) {
        height = Math.max(GUIConstants.MINIMUM_HEIGHT, Math.min(GUIConstants.MAXIMUM_HEIGHT, height));

        if (height < GUIConstants.BLUE_HEIGHT) {
            double ratio = normalize(height, GUIConstants.MINIMUM_HEIGHT, GUIConstants.BLUE_HEIGHT);
            return new Color(0, (int) (255 * ratio), 255);
        } else if (height < GUIConstants.GREEN_HEIGHT) {
            double ratio = normalize(height, GUIConstants.BLUE_HEIGHT, GUIConstants.GREEN_HEIGHT);
            return new Color(0, 255, (int) (255 * (1 - ratio)));
        } else if (height < GUIConstants.YELLOW_HEIGHT) {
            double ratio = normalize(height, GUIConstants.GREEN_HEIGHT, GUIConstants.YELLOW_HEIGHT);
            return new Color((int) (255 * ratio), 255, 0);
        } else {
            double ratio = normalize(height, GUIConstants.YELLOW_HEIGHT, GUIConstants.MAXIMUM_HEIGHT);
            return new Color(255, (int) (255 * (1 - ratio)), 0);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int y = 0; y < getHeight(); y++) {
            double height = pixelToHeight(y);
            g.setColor(getElevationColor(height));
            g.drawLine(0, y, getWidth(), y);
        }

        g.setColor(Color.BLACK);
        g.drawLine(0, selectedY, getWidth(), selectedY);
    }

    public static void promptHeightSlider(Consumer<Double> onAccept) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Select Elevation");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            JLabel label = new JLabel("Height: 0.0", SwingConstants.CENTER);
            label.setOpaque(true);
            label.setBackground(getElevationColor(0));
            label.setForeground(Color.BLACK);
            label.setFont(new Font("Arial", Font.BOLD, 16));
            frame.add(label, BorderLayout.NORTH);

            final double[] selectedHeight = {0};

            ElevationSlider slider = new ElevationSlider(height -> {
                selectedHeight[0] = height;
                label.setText(String.format("Height: %.2f", height));
                label.setBackground(getElevationColor(height));
            }, label);
            frame.add(slider, BorderLayout.CENTER);

            JButton acceptButton = new JButton("Accept");
            acceptButton.addActionListener(_ -> {
                frame.dispose();
                onAccept.accept(selectedHeight[0]);
            });
            frame.add(acceptButton, BorderLayout.SOUTH);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

}
