package logic.graph_objects;

import logic.communication.Transmitter;
import logic.shapes.Position;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class Node implements Iterable<Edge>, Serializable {
    @Serial private static final long serialVersionUID = 1L;

    public final int id;
    private final List<Edge> edges = new ArrayList<>();
    private final Set<Node> neighbors = new HashSet<>();

    public final Position position;
    public final Transmitter transmitter = new Transmitter();


    public Node(int id, Position pos) {
        this.id = id;
        this.position = pos;
    }

    public double x() { return position.x; }
    public double y() { return position.y; }
    public double distanceTo(Node other) { return position.distance(other.position); }

    @Override
    public Iterator<Edge> iterator() { return edges.iterator(); }

    public void addEdge(Edge e) { edges.add(e); neighbors.add(e.getOther(this)); }
    public void removeEdge(Edge e) { edges.remove(e); neighbors.remove(e.getOther(this)); }
    public int degree() { return edges.size(); }

    public boolean hasNeighbour(Node other) { return neighbors.contains(other); }
    public Iterable<Node> getNeighbors() {
        return neighbors;
    }

}
