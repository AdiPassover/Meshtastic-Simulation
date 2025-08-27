package GUI;

import logic.physics.Position;

import java.awt.*;

public record ScreenTransform(double x, double y, double zoom) {
  public static ScreenTransform createFromRequirement(Position worldPosition, Point screenLocation, double zoom) {
    return new ScreenTransform(worldPosition.x - screenLocation.x / zoom, worldPosition.y - screenLocation.y / zoom, zoom);
  }

  public Position screenToWorld(Point p) {
    if (p == null) return null;
    return new Position(p.x / zoom + x, p.y / zoom + y);
  }

  public Point worldToScreen(Position p) {
    if (p == null) return null;
    return new Point((int) (zoom * (p.x - x)), (int) (zoom * (p.y - y))); // TODO: maybe round to nearest, don't think it should matter
  }
}
