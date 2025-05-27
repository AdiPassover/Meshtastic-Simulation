package logic.graph_objects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {

    public final Map<Integer, Node> nodes = new HashMap<>();
    private final Set<Edge> edges = new HashSet<>();

    public Graph() {}

    public void addNode(Node node) {
        nodes.put(node.id.hashCode(), node);
    }
    public boolean hasNode(Node node) {
        return nodes.containsKey(node.id.hashCode());
    }
    public void addEdge(Edge edge) {
        edges.add(edge);
        edge.node1.addEdge(edge);
        edge.node2.addEdge(edge);
    }

}
