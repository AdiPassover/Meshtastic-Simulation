package logic.communication;

import logic.graph_objects.Node;

public class Transmission {

    public final Node source;
    public final Message message;

    public Transmission(Message message, Node source) {
        this.message = message;
        this.source = source;
    }

}
