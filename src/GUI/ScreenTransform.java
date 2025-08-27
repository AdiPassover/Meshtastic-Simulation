package GUI;

import logic.physics.Position;

import java.awt.*;

public record ScreenTransform(double x, double y, double zoom) {
  // for now, assume zoom == 1
  public Position screenToWorld(Point p) {  // TODO: should be used after mouse click almost everywhere
    if (p == null) return null;
    return new Position(p.x + x, p.y + y);
  }

  public Point worldToScreen(Position p) {
    if (p == null) return null;
    return new Point((int) (p.x - x), (int) (p.y - y)); // TODO: maybe round to nearest, don't think it should matter
  }
}
