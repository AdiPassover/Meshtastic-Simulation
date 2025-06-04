package logic;

import logic.graph_objects.Node;

public class PhysicsEngine {

    public double probabilityOfMessagePassing(Node source, Node receiver) {
        return 0.5;
    }

    public double probabilityOfSurvivingCollision(int numMessagesCollided) {
        return 1.0 / numMessagesCollided;
    }

}
