package logic.communication.transmitters;

import logic.graph_objects.Node;

import java.awt.*;
import java.util.function.Function;

public enum TransmitterType {
    DEAD(DeadTransmitter::new, Color.RED),
    FLOODING(FloodingTransmitter::new, Color.BLUE);

    private final Function<Node, Transmitter> constructor;
    private final Color color;

    TransmitterType(Function<Node, Transmitter> constructor, Color color) {
        this.constructor = constructor;
        this.color = color;
    }

    public Transmitter create(Node owner) { return constructor.apply(owner); }
    public Color getColor() { return color; }


    public static String[] getTransmitterTypeNames() {
        return java.util.Arrays.stream(TransmitterType.values())
                .map(Enum::name)
                .toArray(String[]::new);
    }

    public static TransmitterType fromString(String name) {
        return java.util.Arrays.stream(TransmitterType.values())
                .filter(type -> type.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown TransmitterType: " + name));
    }
}
