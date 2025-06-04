package logic.communication.transmitters;

import logic.graph_objects.Node;

public class TransmittersFactory {


    public static Transmitter createTransmitter(TransmitterType type, Node owner) {
        return type.create(owner);
    }

}
