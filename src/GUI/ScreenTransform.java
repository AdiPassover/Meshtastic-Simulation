package GUI;

import logic.physics.Position;

import java.awt.*;

public record ScreenTransform(double x, double y, double zoom) {
  public static ScreenTransform createFromRequirement(Position worldPosition, Point screenLocation, double zoom) {
    return new ScreenTransform(worldPosition.x - screenLocation.x / zoom, worldPosition.y - screenLocation.y / zoom, zoom);
  }

  public Position screenToWorld(Point p) {
    if (p == null) return null;
    return screenToWorld(p.x, p.y);
  }
  public Position screenToWorld(int x, int y) {
    return new Position(x / zoom + this.x, y / zoom + this.y);
  }

  public Point worldToScreen(Position p) {
    if (p == null) return null;
    return worldToScreen(p.x, p.y);
  }
  public Point worldToScreen(double x, double y) {
    return new Point((int) (zoom * Math.round(x - this.x)), (int) Math.round(zoom * (y - this.y)));
  }

}
