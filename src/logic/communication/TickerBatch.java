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

  public boolean isFinished() {
    return tickers.stream().allMatch(Ticker::isFinished);
  }

  public void tick() {
    tickers.forEach(Ticker::tick);
  }

  public Statistics.AverageStatistics getStatistics() {
    return Statistics.createAverage(tickers.stream().map(Ticker::getStatistics).toList());
  }

  public int getCurrentTick() {
    // TODO: is this enough? all current ticks should be the same
    return tickers.getFirst().getCurrentTick();
  }

  public Map<Message, Node> getMessagesReceivedThisTick() {
    // TODO: is this good as a representative or get all (will certainly be too many and might require changes)
    return tickers.getFirst().getMessagesReceivedThisTick();
  }
}
