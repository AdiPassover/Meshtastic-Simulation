package logic.communication;

import logic.graph_objects.Node;

public class Transmission {

    private final Message message;
    private final Node source;

    public Transmission(Message message, Node source) {
        this.message = message;
        this.source = source;
    }

}
