package GUI;

import GUI.shapesGUI.BlockGUI;
import GUI.shapesGUI.NodeGUI;
import GUI.shapesGUI.ShapeGUI;
import logic.communication.Scheduler;
import logic.communication.transmitters.TransmitterType;
import logic.graph_objects.Node;
import logic.physics.Block;
import logic.physics.PhysicsEngine;
import logic.physics.Position;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphGenerator {

    private Random rand;

    private static final int WORLD_SIZE = 1000;

    private static final Polygon[] PRESET_BLOCK_SHAPE = {
            // Triangle
            new Polygon(new int[]{0, 50, 25}, new int[]{0, 0, 43}, 3),
            // Right Angled Triangle
            new Polygon(new int[]{0, 50, 0}, new int[]{0, 0, 50}, 3),
            // Square
            new Polygon(new int[]{0, 50, 50, 0}, new int[]{0, 0, 50, 50}, 4),
            // Rectangle
            new Polygon(new int[]{0, 70, 70, 0}, new int[]{0, 0, 50, 50}, 4),
            // Pentagon
            new Polygon(new int[]{25, 50, 40, 10, 0}, new int[]{0, 15, 45, 45, 15}, 5),
    };


    public GraphGenerator(long seed) {
        rand = new Random(seed);
    }

    public List<ShapeGUI> generate(int numNodes, int numBlocks, int numMessages, TransmitterType transmitterType, boolean[] isMessageTick) {
        List<Block> blocks = generateBlocks(numBlocks);
        List<Node> nodes = generateNodes(numNodes, blocks, transmitterType);

        scheduleMessages(nodes, numMessages, isMessageTick);

        List<ShapeGUI> graph = new ArrayList<>();
        for (Node n : nodes) graph.add(new NodeGUI(n));
        for (Block b : blocks) graph.add(new BlockGUI(b));

        return graph;
    }

    private List<Node> generateNodes(int numNodes, List<Block> blocks, TransmitterType type) {
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < numNodes; ++i) {
            double x = rand.nextDouble() * WORLD_SIZE;
            double y = rand.nextDouble() * WORLD_SIZE;
            nodes.add(new Node(i, new Position(x, y, PhysicsEngine.getHeightAt(x, y, blocks)), type));
        }
        return nodes;
    }

    private List<Block> generateBlocks(int numBlocks) {
        List<Block> blocks = new ArrayList<>();

        for (int i = 0; i < numBlocks; ++i) {
            double x = rand.nextDouble() * WORLD_SIZE;
            double y = rand.nextDouble() * WORLD_SIZE;
            double height = rand.nextGaussian(0, 50);
            height = Math.max(height, Constants.MINIMUM_HEIGHT);
            height = Math.min(height, Constants.MAXIMUM_HEIGHT);
            int shapeIndex = rand.nextInt(PRESET_BLOCK_SHAPE.length);
            Polygon shape = new Polygon(PRESET_BLOCK_SHAPE[shapeIndex].xpoints,
                                        PRESET_BLOCK_SHAPE[shapeIndex].ypoints,
                                        PRESET_BLOCK_SHAPE[shapeIndex].npoints);
            shape.translate((int) x, (int) y);

            blocks.add(new Block(new Polygon(shape.xpoints.clone(), shape.ypoints.clone(), shape.npoints), height));
        }

        return blocks;
    }

    private void scheduleMessages(List<Node> nodes, int numMessagesPerTick, boolean[] isMessageTick) {
        for (int tick = 0; tick < isMessageTick.length; ++tick) {
            if (!isMessageTick[tick]) continue;

            List<Node> sendingNodes = new ArrayList<>(nodes);
            while (sendingNodes.size() > numMessagesPerTick)
                sendingNodes.remove(rand.nextInt(sendingNodes.size()));

            for (Node n : sendingNodes) {
                int destId = rand.nextInt(nodes.size());
                while (destId == n.id) destId = rand.nextInt(nodes.size());
                String payload = "Msg from " + n.id + " at tick " + tick;
                n.getTransmitter().scheduleMessage(payload, destId, tick);
            }
        }
    }

}
