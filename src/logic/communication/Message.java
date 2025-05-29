package logic.communication;

import logic.graph_objects.Node;

public class Message {

    public final Node sourceId;           // ID of origin node
    public final String payload;         // Actual message content
    public final int ttl;                // Time-To-Live
    public final long createdTick;       // Tick when message was created

    public Message(Node source, String payload, int ttl, long createdTick) {
        this.sourceId = source;
        this.payload = payload;
        this.ttl = ttl;
        this.createdTick = createdTick;
    }

}
