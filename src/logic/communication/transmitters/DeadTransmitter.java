package logic.communication.transmitters;

import logic.communication.Message;
import logic.communication.Transmission;
import logic.graph_objects.Node;

import java.util.HashSet;
import java.util.Set;

public class DeadTransmitter extends Transmitter {

    public DeadTransmitter(Node owner) { super(owner); }

    @Override
    public void start() {}

    @Override
    public Transmission transmit(int currentTick) { return null; }

    @Override
    public void receive(Transmission msg, int currentTick) {}

    @Override
    public boolean isScheduleEmpty(int currentTick) { return true; }

}
