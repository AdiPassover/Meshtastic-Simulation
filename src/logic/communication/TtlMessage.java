package logic.communication;

public class TtlMessage extends Message {

    public final int ttl;

    public TtlMessage(int sourceId, String payload, int ttl, int destinationId, int currentTick) {
        super(sourceId, payload, destinationId, currentTick);
        this.ttl = ttl;
    }

    public TtlMessage(int sourceId, String payload, int ttl, int destinationId, String header, int currentTick) {
        super(sourceId, payload, destinationId, header, currentTick);
        this.ttl = ttl;
    }

    public Message forward() {
        return new TtlMessage(sourceId, payload, ttl-1, destinationId, creationTick);
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
