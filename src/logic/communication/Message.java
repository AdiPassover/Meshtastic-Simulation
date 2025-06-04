package logic.communication;

import logic.graph_objects.Node;

import java.io.Serializable;

public class Message implements Serializable {

    public final String payload;         // Actual message content
    public final int ttl;                // Time-To-Live
    public final int sourceId;            // ID of origin node
    public final int destinationId;      // ID of destination node, -1 if broadcast
    public final String header;         // Header information, e.g., "Broadcast" or "Unicast"

    public Message(int sourceId, String payload, int ttl, int destinationId) {
        this.sourceId = sourceId;
        this.payload = payload;
        this.ttl = ttl;
        this.destinationId = destinationId;
        this.header = null;
    }

    public Message(int sourceId, String payload, int ttl, int destinationId, String header) {
        this.sourceId = sourceId;
        this.payload = payload;
        this.ttl = ttl;
        this.destinationId = destinationId;
        this.header = header;
    }

    public Message forward() {
        return new Message(sourceId, payload, ttl-1, destinationId);
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