package logic.graph_objects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {

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

}
