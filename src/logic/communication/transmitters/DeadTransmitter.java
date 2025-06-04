package logic.communication.transmitters;

import logic.communication.Transmission;
import logic.graph_objects.Node;

public class DeadTransmitter extends Transmitter {

    public DeadTransmitter(Node owner) {
        super(owner); // No owner, as this transmitter does not function
    }


    @Override
    public Transmission transmit(long currentTick) { return null; }

    @Override
    public void receive(Transmission msg, long currentTick) {}

    @Override
    public void scheduleMessage(String payload, int destinationId, long sendTick) {}

    @Override
    public void clearSchedule() {}

}
