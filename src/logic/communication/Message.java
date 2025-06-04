package logic.communication;

import logic.graph_objects.Node;

import java.io.Serializable;

public class Message implements Serializable {

    public final String payload;         // Actual message content
    public final int ttl;                // Time-To-Live
    public final int sourceId;            // ID of origin node
    public final int destinationId;      // ID of destination node, -1 if broadcast
    public final String header;
    public final int creationTick;

    public Message(int sourceId, String payload, int ttl, int destinationId, int currentTick) {
        this.sourceId = sourceId;
        this.payload = payload;
        this.ttl = ttl;
        this.destinationId = destinationId;
        this.header = null;
        this.creationTick = currentTick;
    }

    public Message(int sourceId, String payload, int ttl, int destinationId, String header, int currentTick) {
        this.sourceId = sourceId;
        this.payload = payload;
        this.ttl = ttl;
        this.destinationId = destinationId;
        this.header = header;
        this.creationTick = currentTick;
    }

    public Message forward() {
        return new Message(sourceId, payload, ttl-1, destinationId, creationTick);
    }

    @Override
    public String toString() {
        return "Message{" +
                "payload='" + payload + '\'' +
                ", ttl=" + ttl +
                ", sourceId=" + sourceId +
                ", destinationId=" + destinationId +
                ", header='" + header + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        // hash everything but ttl
        int result = payload != null ? payload.hashCode() : 0;
        result = 31 * result + sourceId;
        result = 31 * result + destinationId;
        result = 31 * result + (header != null ? header.hashCode() : 0);
        return result;
    }

}