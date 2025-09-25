package logic.communication.transmitters;

import logic.communication.Message;
import logic.communication.Transmission;
import logic.communication.TtlMessage;
import logic.graph_objects.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GossipingTransmitter extends Transmitter {

    public final static int DEFAULT_TTL = 5;
    public final static float DEFAULT_GOSSIP_PROBABILITY = 0.5f;

    public final int START_TTL;
    public final float GOSSIP_PROBABILITY;
    private final Map<Integer, Message> scheduledMessages = new HashMap<>();
    private final Set<Integer> receivedMessageHashes = new HashSet<>();


    public GossipingTransmitter(Node owner, int startTtl, float gossipProbability) {
        super(owner);
        this.START_TTL = startTtl;
        this.GOSSIP_PROBABILITY = gossipProbability;
    }
    public GossipingTransmitter(Node owner) { this(owner, DEFAULT_TTL, DEFAULT_GOSSIP_PROBABILITY); }
    public GossipingTransmitter(Node owner, int startTtl) { this(owner, startTtl, DEFAULT_GOSSIP_PROBABILITY); }
    public GossipingTransmitter(Node owner, float gossipProbability) { this(owner, DEFAULT_TTL, gossipProbability); }


    @Override
    public void start() {
        // Copy original schedule and convert to TtlMessages
        for (Message msg : getAllOriginalScheduledMessages()) {
            scheduledMessages.put(msg.creationTick, new TtlMessage(
                    msg.sourceId, msg.payload, START_TTL, msg.destinationId, msg.header, msg.creationTick));
        }
    }

    @Override
    public Transmission transmit(int currentTick) {
        Message msg = scheduledMessages.get(currentTick);
        if (msg == null) return null;
        return new Transmission(msg, owner);
    }

    @Override
    public void receive(Transmission tx, int currentTick) {
        if (!(tx.message instanceof TtlMessage msg)) return; // Ignore non-TTL messages
        if (receivedMessageHashes.contains(msg.hashCode())) return; // Ignore already received messages
        receivedMessageHashes.add(msg.hashCode());

        if (msg.destinationId == owner.id || (msg.destinationId == -1 && msg.sourceId != owner.id))
        {
            // If the message is for this node, stop forwarding
            System.out.println("On tick " + currentTick + " transmitter " + owner.id + " received: " + msg + " from " + tx.source.id);
            if (msg.destinationId != -1) return;
        }
        if (msg.ttl <= 0) return; // Ignore expired messages

        // Forward the message to all neighbors
        for (int t = currentTick + 1; true; t++) {
            if (!scheduledMessages.containsKey(t)) {
                scheduledMessages.put(t, msg.forward());
                break;
            }
        }
    }

    @Override
    public boolean isScheduleEmpty(int currentTick) {
        Set<Integer> keys = scheduledMessages.keySet();
        for (Integer key : keys) {
            if (key >= currentTick) return false;
        }
        return true;
    }

}
