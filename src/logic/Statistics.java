package logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Statistics {

    private final int graphSize;

    private int totalTransmissions;
    private final Set<Integer> originalMessagesHashes;
    private final Set<Integer> successfulMessageHashes;
    private int latenciesTotal;
    private int numCollisions;
    private final Map<Integer, Set<Integer>> broadcastMessages; // Maps message hash to number of ids who received it

    public Statistics(int size) {
        this.graphSize = size;

        this.totalTransmissions = 0;
        this.originalMessagesHashes = new HashSet<>();
        this.successfulMessageHashes = new HashSet<>();
        this.latenciesTotal = 0;
        this.numCollisions = 0;
        this.broadcastMessages = new HashMap<>();
    }

    public void addTransmission() { totalTransmissions += 1; }
    public void addOriginalMessage(int messageHash) { originalMessagesHashes.add(messageHash); }
    public void addSuccessfulMessage(int messageHash, int latency) {
        boolean added = successfulMessageHashes.add(messageHash);
        if (added) addLatency(latency);
    }
    public void addLatency(int latency) {
        latenciesTotal += latency;
    }
    public void addCollisions(int numCollisions) { this.numCollisions += numCollisions; }

    public int getTotalTransmissions() {
        return totalTransmissions;
    }
    public int getOriginalMessages() {
        return originalMessagesHashes.size();
    }
    public int getSuccessfulMessages() {
        return successfulMessageHashes.size();
    }
    public double getAverageLatency() {
        return successfulMessageHashes.isEmpty() ? 0 : (double) latenciesTotal / successfulMessageHashes.size();
    }
    public int getNumCollisions() {
        return numCollisions;
    }

    public void printStats() {
        System.out.println("Total transmissions: " + getTotalTransmissions());
        System.out.println("Original messages: " + getOriginalMessages());
        System.out.println("Successful messages: " + getSuccessfulMessages());
        // System.out.println("Message success rate: " + successfulMessages / originalMessages);
        System.out.println("Average latency: " + getAverageLatency());
        System.out.println("Number of collisions: " + getNumCollisions());
    }

    public void addBroadcastMessage(int msgHash) {
        broadcastMessages.put(msgHash, new HashSet<>());
    }

    public void addSuccessfulBroadcast(int msgHash, int receiverId, int latency) {
        Set<Integer> nodes = broadcastMessages.get(msgHash);
        if (nodes != null) {
            nodes.add(receiverId);
        } else {
            nodes = new HashSet<>();
            nodes.add(receiverId);
            broadcastMessages.put(msgHash, nodes);
        }

        if (nodes.size() == graphSize - 1)
            addSuccessfulMessage(msgHash, latency);
    }
}
