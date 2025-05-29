package logic.communication.transmitters;


import logic.communication.Message;

import java.util.*;

public class FloodingTransmitter extends Transmitter {

    private final Queue<Message> outbox = new LinkedList<>();
    private final Set<Integer> seenMessages = new HashSet<>();

    @Override
    public void receive(Message msg, long currentTick) {
//        if (seenMessages.contains(msg.getId())) return;
//
//        seenMessages.add(msg.getId());
//
//        if (!msg.isExpired()) {
//            Message forwarded = msg.forwardFrom(owner);
//            outbox.add(forwarded);
//        }
    }

    @Override
    public Message transmit(long currentTick) {
        if (!outbox.isEmpty())
            return outbox.poll();
        return null;
    }

    @Override
    public void scheduleMessage(String payload, long sendTick) {
        // We'll assume the simulation engine will call transmit() at each tick
        // and trigger the payload at the correct tick via some scheduler mechanism
        Message msg = new Message(owner, payload, 5, sendTick);
        outbox.add(msg);
    }


}

