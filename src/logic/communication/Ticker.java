package logic.communication;

import logic.PhysicsEngine;
import logic.graph_objects.Graph;
import logic.graph_objects.Node;

import java.util.*;

public class Ticker {

    private final PhysicsEngine physics = new PhysicsEngine();
    private final Graph graph;
    private long currentTick;

    private long collisionCount = 0;

    public Ticker(Graph graph) {
        this.graph = graph;
        this.currentTick = 0L;
    }

    public long getCurrentTick() { return currentTick; }

    // TODO count collisions, successful transmissions, latency, etc.

    public void tick() {

        // 1. Transmit messages from nodes
        List<Transmission> activeTransmissions = new ArrayList<>();
        for (Node node : graph) {
            Transmission transmission = node.getTransmitter().transmit(currentTick);
            if (transmission == null) continue;
            activeTransmissions.add(transmission);
        }

        // 2. Deliver transmissions
        Map<Node, List<Transmission>> received = new HashMap<>();
        for (Transmission tx : activeTransmissions) {
            for (Node receiver : graph) {
                if (receiver == tx.source) continue;

                double prob = physics.probabilityOfMessagePassing(tx.source, receiver);
                if (Math.random() < prob) {
                    List<Transmission> transmissions = received.get(receiver);
                    if (transmissions != null) {
                        transmissions.add(tx);
                    } else {
                        transmissions = new ArrayList<>();
                        transmissions.add(tx);
                        received.put(receiver, transmissions);
                    }
                }
            }
        }

        // 3. Check for collisions
        for (var entry : received.entrySet()) {
            Node receiver = entry.getKey();
            List<Transmission> transmissions = entry.getValue();

            if (transmissions.size() == 1) {
                receiver.getTransmitter().receive(transmissions.remove(0), currentTick);
            } else if (transmissions.size() > 1) { // Collision detected
                collisionCount++;
                double prob = physics.probabilityOfSurvivingCollision(transmissions.size());
                for (Transmission tx : transmissions)
                    if (Math.random() < prob) receiver.getTransmitter().receive(tx, currentTick);
            }
        }

        currentTick++;
    }

}
