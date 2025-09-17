package logic.communication.transmitters;

import logic.communication.Message;
import logic.communication.Transmission;
import logic.graph_objects.Node;

import java.util.HashSet;
import java.util.Set;

public class DeadTransmitter extends Transmitter {

    private final Set<Message> allMessages = new HashSet<>();

    public DeadTransmitter(Node owner) {
        super(owner); // No owner, as this transmitter does not function
    }


    @Override
    public Transmission transmit(int currentTick) { return null; }

    @Override
    public void receive(Transmission msg, int currentTick) {}

    @Override
    public void scheduleMessage(String payload, int destinationId, int sendTick) {
        // Store the message for statistics, but do not schedule it
        Message message = new Message(owner.id, payload, 0, destinationId, sendTick);
        allMessages.add(message);
    }

    @Override
    public void clearSchedule() {}

    @Override
    public boolean isScheduleEmpty(int currentTick) { return true; }

    @Override
    public Set<Message> getAllOriginalScheduledMessages() {
        return new HashSet<>(allMessages);
    }

}
