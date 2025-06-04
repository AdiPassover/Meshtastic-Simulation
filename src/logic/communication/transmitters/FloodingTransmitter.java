package logic.communication.transmitters;

import logic.communication.Message;
import logic.communication.Transmission;
import logic.graph_objects.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

public class FloodingTransmitter extends Transmitter {

    private final Map<Integer, Message> scheduledMessages = new HashMap<>();

    private static final int DEFAULT_TTL = 5;

    private final Set<Integer> receivedMessageHashes = new HashSet<>();

    public FloodingTransmitter(Node owner) {
        super(owner); // Owner will be set later
    }


    @Override
    public Transmission transmit(int currentTick) {
        Message msg = scheduledMessages.get(currentTick);
        if (msg == null) return null;
        return new Transmission(msg, owner);
    }

    @Override
    public void receive(Transmission tx, int currentTick) { // TODO how to count successful transmissions?
        Message msg = tx.message;
        if (receivedMessageHashes.contains(msg.hashCode())) return;
        receivedMessageHashes.add(msg.hashCode());
        if (msg.destinationId == owner.id || (msg.destinationId == -1 && msg.sourceId != owner.id))
        {
            // If the message is for this node, stop forwarding
            System.out.println("On tick " + currentTick + " transmitter " + owner.id + " received: " + msg + " from " + tx.source.id);
            if (msg.destinationId != -1) return;
        }
        if (msg.ttl <= 0) return; // Ignore expired messages

        // Forward the message to all neighbors
        for (int t = currentTick+1; true; t++) {
            if (!scheduledMessages.containsKey(t)) {
                scheduledMessages.put(t, msg.forward());
                break;
            }
        }
    }

    @Override
    public void scheduleMessage(String payload, int destinationId, int sendTick) {
        Message msg = new Message(owner.id, payload, DEFAULT_TTL, destinationId, sendTick);
        scheduledMessages.put(sendTick, msg);
    }

    @Override
    public void clearSchedule() {
        scheduledMessages.clear();
    }

    @Override
    public boolean isScheduleEmpty() {
        return scheduledMessages.isEmpty();
    }

}
