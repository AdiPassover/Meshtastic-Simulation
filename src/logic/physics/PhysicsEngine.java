package logic.physics;

import GUI.GUIConstants;
import logic.LogicConstants;
import logic.graph_objects.Node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PhysicsEngine implements Serializable {

    private final List<Block> blocks;


    public PhysicsEngine(List<Block> blocks) {
        this.blocks = blocks;
    }
    public PhysicsEngine() { this(new ArrayList<>()); }


    public void addBlock(Block block) {
        blocks.add(block);
    }
    public void clearBlocks() {
        blocks.clear();
    }


    private boolean hasLineOfSight(Node source, Node receiver) {
        for (Block block : blocks) {
            if (block.intersectsLine(source.position, receiver.position))
                return false;
        }
        return true;
    }

    public double probabilityOfMessagePassing(Node source, Node receiver) {
        if (!hasLineOfSight(source, receiver)) return 0.0;
        double d = source.position.distance(receiver.position);
        return probMessagePassingWithLOS(d);
    }

    public static double probMessagePassingWithLOS(double distance) {
        if (distance >= LogicConstants.TRANSMITTER_MAX_RANGE) return 0.0;
        double ratio = distance / LogicConstants.TRANSMITTER_MAX_RANGE;
        if (ratio <= LogicConstants.CLOSE_RANGE_FRAC) return 1.0;
        double scaled = (ratio - LogicConstants.CLOSE_RANGE_FRAC) / (1.0 - LogicConstants.CLOSE_RANGE_FRAC); // normalize to [0,1] across falloff zone
        return Math.pow(1.0 - scaled, 2);
    }

    public double probabilityOfSurvivingCollision(int numMessagesCollided) {
        return 1.0 / numMessagesCollided;
    }

    public boolean shouldAddEdge(Node source, Node receiver) {
        return probabilityOfMessagePassing(source, receiver) > LogicConstants.EDGE_PROBABILITY_THRESHOLD;
    }

    public double getHeightAt(double x, double y) {
        return getHeightAt(x, y, blocks);
    }

    public static double getHeightAt(double x, double y, List<Block> blocks) {
        double maxHeight = GUIConstants.MINIMUM_HEIGHT - 1.0;
        for (Block b : blocks)
            if (b.polygon.contains(x, y)) maxHeight = Math.max(maxHeight, b.height);
        return maxHeight == GUIConstants.MINIMUM_HEIGHT - 1.0 ? 0.0 : maxHeight;
    }

}
