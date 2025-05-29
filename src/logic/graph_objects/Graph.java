package logic.graph_objects;

import java.util.*;

public class Graph implements Iterable<Node> {

    private final Map<Integer, Node> nodes = new HashMap<>();
    private final Set<Edge> edges = new HashSet<>();

    public Graph() {}

    public void addNode(Node node) {
        nodes.put(node.id, node);
    }
    public boolean hasNode(Node node) {
        return nodes.containsKey(node.id);
    }
    public void addEdge(Node node1, Node node2) {
        Edge edge = new Edge(node1, node2);
        edges.add(edge);
        edge.node1.addEdge(edge);
        edge.node2.addEdge(edge);
    }

    public int size() { return nodes.size(); }

    @Override
    public Iterator<Node> iterator() {
        return nodes.values().iterator();
    }

}
