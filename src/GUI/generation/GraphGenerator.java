package GUI.generation;

import GUI.GUIConstants;
import GUI.shapesGUI.BlockGUI;
import GUI.shapesGUI.NodeGUI;
import GUI.shapesGUI.ShapeGUI;
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

    private final Random rand;

    private static final int WORLD_WIDTH = 3850;
    private static final int WORLD_HEIGHT = 2500;

    private static final Polygon[] PRESET_BLOCK_SHAPE = {
            new Polygon(new int[]{0, 50, 25}, new int[]{0, 0, 43}, 3),                  // Triangle
            new Polygon(new int[]{0, 50, 0}, new int[]{0, 0, 50}, 3),                   // Right Angled Triangle
            new Polygon(new int[]{0, 50, 50, 0}, new int[]{0, 0, 50, 50}, 4),           // Square
            new Polygon(new int[]{0, 70, 70, 0}, new int[]{0, 0, 50, 50}, 4),           // Rectangle
            new Polygon(new int[]{25, 50, 40, 10, 0}, new int[]{0, 15, 45, 45, 15}, 5) // Pentagon
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
            double x = rand.nextDouble() * WORLD_WIDTH;
            double y = rand.nextDouble() * WORLD_HEIGHT;
            nodes.add(new Node(i, new Position(x, y, PhysicsEngine.getHeightAt(x, y, blocks)), type));
        }
        return nodes;
    }

    private List<Block> generateBlocks(int numBlocks) {
        List<Block> blocks = new ArrayList<>();

        for (int i = 0; i < numBlocks; ++i) {
            double x = sampleGaussianInRange(WORLD_WIDTH*0.5, WORLD_WIDTH*0.2, 0, WORLD_WIDTH);
            double y = sampleGaussianInRange(WORLD_HEIGHT*0.5, WORLD_HEIGHT*0.2, 0, WORLD_HEIGHT);
            double height = sampleGaussianInRange(30, 25, GUIConstants.MINIMUM_HEIGHT, GUIConstants.MAXIMUM_HEIGHT);  // slightly skewed upwards to block more lines
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

    private double sampleGaussianInRange(double mean, double std, double min, double max) {
        double value = rand.nextGaussian(mean, std);
        return Math.clamp(value, min, max);
    }

}
