package logic.communication;

import GUI.MainSimulationWindow;
import logic.graph_objects.Graph;
import logic.graph_objects.Node;

import java.util.ArrayList;
import java.util.List;

public class Ticker {

    private final Graph graph;
    private final List<Transmission> activeTransmissions = new ArrayList<>();
    private long currentTick;

    public Ticker(Graph graph) {
        this.graph = graph;
        this.currentTick = 0L;
    }


//    public void tick() {
//        for (Node node : graph) {
//            Message msg = node.getTransmitter().transmit(currentTick);
//            if (msg == null) continue;
//            Transmission tx = new Transmission(node, msg, currentTick, DEFAULT_DURATION);
//            activeTransmissions.add(tx);
//        }
//
//        // 2. Deliver transmissions
//        for (Transmission tx : activeTransmissions) {
//            if (!tx.isActive(currentTick)) continue;
//
//            for (Node receiver : graph) {
//                if (receiver == tx.sender) continue;
//                if (tx.hasAlreadyDeliveredTo(receiver)) continue;
//
//                double prob = computeSuccessProbability(tx.sender, receiver);
//                if (Math.random() < prob) {
//                    receiver.transmitter.receive(tx.message, currentTick);
//                    tx.markDeliveredTo(receiver);
//                }
//            }
//        }
//
//        // 3. Clean up expired transmissions
//        activeTransmissions.removeIf(tx -> !tx.isActive(currentTick));
//
//        currentTick++;
//    }


}
