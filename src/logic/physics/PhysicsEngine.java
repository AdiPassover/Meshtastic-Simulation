package logic.physics;

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
//        return hasLineOfSight(source, receiver) ?
//                1.0 / (1.0 + source.distanceTo(receiver))*(1.0 + source.distanceTo(receiver)) : 0.0;
        return hasLineOfSight(source, receiver) ? 1.0 : 0.0;
    }

    public double probabilityOfSurvivingCollision(int numMessagesCollided) {
        return 1.0 / numMessagesCollided;
    }

    public double getHeightAt(double x, double y) {
        return getHeightAt(x, y, blocks);
    }

    public static double getHeightAt(double x, double y, List<Block> blocks) {
        double maxHeight = Double.MIN_VALUE;
        for (Block b : blocks)
            if (b.polygon.contains(x, y)) maxHeight = Math.max(maxHeight, b.height);
        return maxHeight == Double.MIN_VALUE ? 0.0 : maxHeight;
    }

}
