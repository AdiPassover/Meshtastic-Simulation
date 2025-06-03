package logic.communication.transmitters;

import logic.communication.Message;
import logic.communication.Transmission;
import logic.graph_objects.Node;

public abstract class Transmitter {

    protected Node owner;

    public void setOwner(Node node) {
        this.owner = node;
    }

    /**
     * Called once per simulation tick. Return null if no message is to be sent.
     */
    public abstract Transmission transmit(long currentTick);

    /** Called when the node receives a message */
    public abstract void receive(Transmission msg, long currentTick);

    /** Called before simulation starts to schedule messages */
    public abstract void scheduleMessage(String payload, int destinationId, long sendTick);
    public abstract void clearSchedule();

}

