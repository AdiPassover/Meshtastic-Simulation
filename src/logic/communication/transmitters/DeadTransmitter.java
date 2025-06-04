package logic.communication.transmitters;

import logic.communication.Transmission;
import logic.graph_objects.Node;

public class DeadTransmitter extends Transmitter {

    public DeadTransmitter(Node owner) {
        super(owner); // No owner, as this transmitter does not function
    }


    @Override
    public Transmission transmit(int currentTick) { return null; }

    @Override
    public void receive(Transmission msg, int currentTick) {}

    @Override
    public void scheduleMessage(String payload, int destinationId, int sendTick) {}

    @Override
    public void clearSchedule() {}

    @Override
    public boolean isScheduleEmpty() { return true; }

}
