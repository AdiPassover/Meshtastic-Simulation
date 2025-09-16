package GUI;

import GUI.elevation.ElevationSlider;
import GUI.shapesGUI.ShapeGUI;
import logic.physics.Position;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.function.Consumer;

public class DrawingPanel extends JPanel {

    private final MainSimulationWindow mainWindow;
    private Consumer<Graphics2D> previewDrawer = null;
    private final Color backgroundColor = ElevationSlider.getElevationColor(0.0);
    private final Image backgroundImage;

    public DrawingPanel(MainSimulationWindow mainWindow) {
      super();
      this.mainWindow = mainWindow;
      Image image;
      try {
          image = ImageIO.read(Constants.BACKGROUND_IMAGE_FILE);
          System.out.println("Loaded image from " + Constants.BACKGROUND_IMAGE_FILE.getCanonicalPath());
      } catch (IOException e) {
          try {
              System.err.println("Error opening background image (from " + Constants.BACKGROUND_IMAGE_FILE.getCanonicalPath() + "), defaulting to solid color.");
          } catch (IOException _) {}
          image = null;
      }
      this.backgroundImage = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.backgroundImage != null) {
            drawBackgroundImages(g);
        } else {
            g.setColor(backgroundColor);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        for (ShapeGUI shape : mainWindow.getShapes()) {
            shape.drawShape((Graphics2D) g, mainWindow.getTransform());
        }

        if (previewDrawer != null)
            previewDrawer.accept((Graphics2D) g);
    }

    private void drawBackgroundImages(Graphics g) {
        // find current display bounds
        Position p0 = mainWindow.getTransform().screenToWorld(new Point(0, 0));
        Position p1 = mainWindow.getTransform().screenToWorld(new Point(getWidth(), getHeight()));

        // get image dimensions
        double zoom = mainWindow.getTransform().zoom();
        double w = backgroundImage.getWidth(null);
        double h = backgroundImage.getHeight(null);

        java.util.function.BiFunction<Double, Double, Double> goodMod = (a, b) -> ((a % b) + b) % b;

        Point start = mainWindow.getTransform().worldToScreen(new Position(p0.x - (goodMod.apply(p0.x, w)), p0.y - (goodMod.apply(p0.y, h))));
        Point end = mainWindow.getTransform().worldToScreen(new Position(p1.x - (goodMod.apply(p1.x, w)) + 1, p1.y - (goodMod.apply(p1.y, h)) + 1));

        int drawX = start.x;
        int drawY = start.y;

        // iterate and draw image repeatedly
        while (drawX < end.x + (w * zoom)) {
            while (drawY < end.y + (h * zoom)) {
                g.drawImage(backgroundImage, drawX, drawY, (int) (w * zoom), (int) (h * zoom), null);
                drawY += (int) (h * zoom);
            }
            drawX += (int) (w * zoom);
            drawY = start.y - (int) (h * zoom);
        }
    }

    public void setPreview(Consumer<Graphics2D> drawer) {
        this.previewDrawer = drawer;
        repaint();
    }

    public void clearPreview() {
        this.previewDrawer = null;
        repaint();
    }
}


