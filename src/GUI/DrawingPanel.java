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
        Position p0 = new Position(mainWindow.getTransform().x(), mainWindow.getTransform().y());
        Position p1 = mainWindow.getTransform().screenToWorld(new Point(getWidth(), getHeight()));
        // get image dimensions
        int w = (int) (mainWindow.getTransform().zoom() * backgroundImage.getWidth(null));
        int h = (int) (mainWindow.getTransform().zoom() * backgroundImage.getHeight(null));

        Point start = mainWindow.getTransform().worldToScreen(new Position(p0.x - (p0.x % w), p0.y - (p0.y % h)));
        Point end = mainWindow.getTransform().worldToScreen(new Position(p1.x - (p1.x % w), p1.y - (p1.y % h)));

        int drawX = start.x - w;
        int drawY = start.y - h;

        // iterate and draw image repeatedly
        while (drawX < end.x + w) {
            while (drawY < end.y + h) {
                g.drawImage(backgroundImage, drawX, drawY, w, h, null);
                drawY += h;
            }
            drawX += w;
            drawY = start.y - h;
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


