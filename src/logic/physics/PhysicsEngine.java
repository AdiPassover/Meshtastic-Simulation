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
            if (block.intersectsLine(source.position, receiver.position)) {
                return false;
            }
        }
        return true;
    }

    public double probabilityOfMessagePassing(Node source, Node receiver) {
        return hasLineOfSight(source, receiver) ? 1.0 : 0.0;
    }

    public double probabilityOfSurvivingCollision(int numMessagesCollided) {
        return 1.0 / numMessagesCollided;
    }

}
