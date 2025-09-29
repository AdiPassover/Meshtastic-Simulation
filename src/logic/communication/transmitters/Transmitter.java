package logic.communication.transmitters;

import logic.communication.Message;
import logic.communication.Transmission;
import logic.graph_objects.Node;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class Transmitter implements Serializable {

    protected final Node owner;

    private final Map<Integer, Message> originalSchedule = new HashMap<>(); // tick -> destinationId

    public Transmitter(Node owner) {
        this.owner = owner;
    }

    /** Called once at the start of the simulation */
    public abstract void start();

    /**
     * Called once per simulation tick. Return null if no message is to be sent.
     */
    public abstract Transmission transmit(int currentTick);

    /** Called when the node receives a message */
    public abstract void receive(Transmission msg, int currentTick);

    /** Called before simulation starts to schedule messages */
    public final void scheduleMessage(int sendTick, String payload, int destinationId) {
        if (originalSchedule.containsKey(sendTick))
            throw new IllegalStateException("Transmitter " + owner.id +
                    " already has a message scheduled at tick " + sendTick);
        originalSchedule.put(sendTick, new Message(owner.id, payload, destinationId, sendTick));
    }
    public final void clearSchedule() {
        originalSchedule.clear();
    }
    public final Message messageScheduledAt(int tick) {
        return originalSchedule.get(tick);
    }
    public final Iterable<Message> getAllOriginalScheduledMessages() {
        return originalSchedule.values();
    }

    public abstract boolean isScheduleEmpty(int currentTick);

}

