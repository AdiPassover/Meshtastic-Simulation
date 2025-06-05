package logic.communication.transmitters;

import logic.communication.Message;
import logic.communication.Transmission;
import logic.graph_objects.Node;

import java.io.Serializable;
import java.util.Set;

public abstract class Transmitter implements Serializable {

    protected final Node owner;

    public Transmitter(Node owner) {
        this.owner = owner;
    }

    /**
     * Called once per simulation tick. Return null if no message is to be sent.
     */
    public abstract Transmission transmit(int currentTick);

    /** Called when the node receives a message */
    public abstract void receive(Transmission msg, int currentTick);

    /** Called before simulation starts to schedule messages */
    public abstract void scheduleMessage(String payload, int destinationId, int sendTick);
    public abstract void clearSchedule();

    public abstract boolean isScheduleEmpty(int currentTick);
    public abstract Set<Message> getAllOriginalScheduledMessages();

}

