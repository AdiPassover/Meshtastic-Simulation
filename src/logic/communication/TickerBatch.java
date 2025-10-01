package logic.communication;

import logic.Statistics;
import logic.graph_objects.Graph;
import logic.graph_objects.Node;
import logic.physics.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TickerBatch {
  private final List<Ticker> tickers;

  public TickerBatch(int count, Graph graph, List<Block> logicBlocks) {
    assert count > 0;
    tickers = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      tickers.add(new Ticker(graph, logicBlocks));
    }
  }

  public boolean isRunning() {
    return tickers.stream().anyMatch(Ticker::isRunning);
  }

  public void tick() {
    tickers.forEach(Ticker::tick);
  }

  public Statistics.AverageStatistics getStatistics() {
    return Statistics.createAverage(tickers.stream().map(Ticker::getStatistics).toList());
  }

  public int getCurrentTick() {
    return tickers.getFirst().getCurrentTick();
  }

  public Map<Message, Node> getMessagesReceivedThisTick() {
    return tickers.getFirst().getMessagesReceivedThisTick();
  }
}
