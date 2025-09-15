package logic.graph_objects;

import logic.communication.transmitters.Transmitter;
import logic.communication.transmitters.TransmitterType;
import logic.physics.Position;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class Node implements Iterable<Edge>, Serializable {
    @Serial private static final long serialVersionUID = 1L;

    public final int id;
    private final List<Edge> edges = new ArrayList<>();
    private final Set<Node> neighbors = new HashSet<>();

    public final Position position;
    private Transmitter transmitter;
    private TransmitterType transmitterType;


    public Node(int id, Position pos) {
        this.id = id;
        this.position = pos;
    }
    public Node(int id, Position pos, TransmitterType type) {
        this(id, pos);
        setTransmitterByType(type);
    }

    public double x() { return position.x; }
    public double y() { return position.y; }
    public double distanceTo(Node other) { return position.distance(other.position); }

    public Transmitter getTransmitter() { return transmitter; }
    public TransmitterType getTransmitterType() { return transmitterType; }
    public void setTransmitterByType(TransmitterType type) {
        transmitter = type.create(this);
        transmitterType = type;
    }

    @Override
    public Iterator<Edge> iterator() { return edges.iterator(); }

    public void addEdge(Edge e) { edges.add(e); neighbors.add(e.getOther(this)); }
    public void removeEdge(Edge e) { edges.remove(e); neighbors.remove(e.getOther(this)); }
    public int degree() { return edges.size(); }

    public boolean hasNeighbour(Node other) { return neighbors.contains(other); }
    public Iterable<Node> getNeighbors() {
        return neighbors;
    }

    @Override
    public String toString() {
        return "Node{id=" + id + ", position=" + position + ", degree=" + degree() + "}";
    }

}
