package logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
        // System.out.println("Message success rate: " + getSuccessfulMessages() / getOriginalMessages());
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

    public static AverageStatistics createAverage(List<Statistics> list) {
        assert !list.isEmpty();
        return new AverageStatistics(
            list.stream().mapToDouble(Statistics::getTotalTransmissions).average().orElseThrow(),
            list.stream().mapToDouble(Statistics::getOriginalMessages).average().orElseThrow(),
            list.stream().mapToDouble(Statistics::getSuccessfulMessages).average().orElseThrow(),
            list.stream().mapToDouble(Statistics::getAverageLatency).average().orElseThrow(),
            list.stream().mapToDouble(Statistics::getNumCollisions).average().orElseThrow());
    }

    public static class AverageStatistics {
        private final double totalTransmissions;
        private final double originalMessages;
        private final double successfulMessages;
        private final double averageLatency;
        private final double numCollisions;
        public AverageStatistics(double totalTransmissions, double originalMessages, double successfulMessages, double averageLatency, double numCollisions) {
          this.totalTransmissions = totalTransmissions;
          this.originalMessages = originalMessages;
          this.successfulMessages = successfulMessages;
          this.averageLatency = averageLatency;
          this.numCollisions = numCollisions;
        }

        public double getTotalTransmissions() {
            return totalTransmissions;
        }

        public double getOriginalMessages() {
            return originalMessages;
        }

        public double getSuccessfulMessages() {
            return successfulMessages;
        }

        public double getAverageLatency() {
            return averageLatency;
        }

        public double getNumCollisions() {
            return numCollisions;
        }
    }
}
