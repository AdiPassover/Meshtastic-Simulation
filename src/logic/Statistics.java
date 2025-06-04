package logic;

public class Statistics {
    public int totalTransmissions;
    public int originalMessages;
    public Set<Integer> successfulMessageHashes;
    public int latenciesTotal;

    public Statistics() {
        this.totalTransmissions = 0;
        this.originalMessages = 0;
        this.successfulMessages = 0;
        this.latenciesTotal = 0;
    }

    public void addTransmission() {
        totalTransmissions += 1;
    }
    public void addOriginalMessage() {
        originalMessages += 1;
    }
    public void addSuccessfulMessage() {
        successfulMessages += 1;
    }
    public void addLatency(int latency) {
        latenciesTotal += latency;
    }

    public void printStats() {
        System.out.println("Total transmissions: " + totalTransmissions);
        System.out.println("Original messages: " + originalMessages);
        System.out.println("Successful messages: " + successfulMessages);
        // System.out.println("Message success rate: " + successfulMessages / originalMessages);
        System.out.println("Average latency: " + (double) latenciesTotal / successfulMessages);
    }
}
