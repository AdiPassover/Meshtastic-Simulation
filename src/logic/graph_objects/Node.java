package logic.graph_objects;

import logic.communication.Transmitter;
import logic.shapes.Position;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Node implements Iterable<Edge>, Serializable {
    @Serial private static final long serialVersionUID = 1L;

    public final String id;
    private final List<Edge> edges = new ArrayList<>();

    public final Position position;
    public final Transmitter transmitter = new Transmitter();


    public Node(String id, Position pos) {
        this.id = id;
        this.position = pos;
    }

    public double distanceTo(Node other) { return position.distance(other.position); }

    @Override
    public Iterator<Edge> iterator() { return edges.iterator(); }

    public void addEdge(Edge e) { edges.add(e); }
    public void removeEdge(Edge e) { edges.remove(e); }
    public int degree() { return edges.size(); }

    public Iterable<Node> getNeighbors() {
        return edges.stream().map(e -> e.getOther(this)).toList();
    }

}
