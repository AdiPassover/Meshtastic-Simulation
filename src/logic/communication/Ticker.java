package logic.communication;

import logic.Statistics;
import logic.physics.Block;
import logic.physics.PhysicsEngine;
import logic.graph_objects.Graph;
import logic.graph_objects.Node;

import java.util.*;

public class Ticker {

    private final PhysicsEngine physics;
    private final Graph graph;
    private int currentTick;
    private final Map<Message, Node> messagesReceivedThisTick = new HashMap<>();

    private final Statistics stats;

    public Ticker(Graph graph, List<Block> blocks) {
        this.graph = graph;
        this.currentTick = 0;
        this.physics = new PhysicsEngine(blocks);

        this.stats = new Statistics(graph.size());
        for (Node node : graph) {
            for (Message message : node.getTransmitter().getAllOriginalScheduledMessages()) {
                stats.addOriginalMessage(message.hashCode());
                if (message.destinationId == -1) stats.addBroadcastMessage(message.hashCode());
            }
        }

        for (Node node : graph) node.getTransmitter().start();
    }

    public int getCurrentTick() { return currentTick; }

    public Statistics getStatistics() { return stats; }

    public void tick() {
        messagesReceivedThisTick.clear();

        // 1. Transmit messages from nodes
        List<Transmission> activeTransmissions = new ArrayList<>();
        for (Node node : graph) {
            Transmission transmission = node.getTransmitter().transmit(currentTick);
            if (transmission == null) continue;
            activeTransmissions.add(transmission);

            stats.addTransmission();
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
            List<Transmission> concurrentTransmissions = entry.getValue();

            double prob = physics.probabilityOfSurvivingCollision(concurrentTransmissions.size());
            stats.addCollisions(concurrentTransmissions.size()-1);  // TODO: should we count collisions by number of messages collided (current) or by number of collisions (size>1)?
            for (Transmission tx : concurrentTransmissions) {
                if (Math.random() < prob) {
                    receiver.getTransmitter().receive(tx, currentTick);
                    messagesReceivedThisTick.put(tx.message, receiver);
                    if (receiver.id == tx.message.destinationId) { // successful delivery
                        stats.addSuccessfulMessage(tx.message.hashCode(), currentTick - tx.message.creationTick);
                    }
                    else if (tx.message.destinationId == -1 && receiver.id != tx.message.sourceId) { // successful broadcast
                        stats.addSuccessfulBroadcast(tx.message.hashCode(), receiver.id, currentTick - tx.message.creationTick);
                    }
                }
            }
        }

        currentTick++;
    }

    public boolean isRunning() {
        return graph.stream().map(Node::getTransmitter).anyMatch(t -> !t.isScheduleEmpty(currentTick));
    }

    public Map<Message, Node> getMessagesReceivedThisTick() {
        return Collections.unmodifiableMap(messagesReceivedThisTick);
    }

}
