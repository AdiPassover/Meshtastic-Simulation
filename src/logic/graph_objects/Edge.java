package logic.graph_objects;

public class Edge {

    public final Node node1, node2;

    public Edge(Node node1, Node node2) {
        this.node1 = node1;
        this.node2 = node2;
    }

    public Node getOther(Node node) {
        if (node.equals(node1)) return node2;
        else if (node.equals(node2)) return node1;
        else throw new IllegalArgumentException("Node is not part of this edge");
    }

}
