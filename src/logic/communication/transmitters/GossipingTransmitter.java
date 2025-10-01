package logic.communication.transmitters;

import logic.communication.Message;
import logic.communication.Transmission;
import logic.graph_objects.Node;

public class GossipingTransmitter extends FloodingTransmitter {

    public final static float DEFAULT_GOSSIP_PROBABILITY = 0.5f;

    public final float GOSSIP_PROBABILITY;


    public GossipingTransmitter(Node owner, int startTtl, float gossipProbability) {
        super(owner, startTtl);
        this.GOSSIP_PROBABILITY = gossipProbability;
    }
    public GossipingTransmitter(Node owner) { this(owner, DEFAULT_TTL, DEFAULT_GOSSIP_PROBABILITY); }
    public GossipingTransmitter(Node owner, int startTtl) { this(owner, startTtl, DEFAULT_GOSSIP_PROBABILITY); }
    public GossipingTransmitter(Node owner, float gossipProbability) { this(owner, DEFAULT_TTL, gossipProbability); }

    @Override
    public Transmission transmit(int currentTick) {
        Message msg = scheduledMessages.get(currentTick);
        if (msg == null) return null;

        // if not original message, decide to send based on probability
        Message originalMsg = messageScheduledAt(currentTick);
        if (originalMsg == null && Math.random() > GOSSIP_PROBABILITY) return null;

        return new Transmission(msg, owner);
    }
}
