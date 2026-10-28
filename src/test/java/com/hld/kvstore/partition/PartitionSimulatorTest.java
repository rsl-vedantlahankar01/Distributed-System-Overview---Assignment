package com.hld.kvstore.partition;

import com.hld.kvstore.node.ClusterNode;
import com.hld.kvstore.node.NodeStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provided test suite. isolateNode()/healNode() are already implemented
 * for you and are covered here to confirm your understanding of how
 * PartitionSimulator interacts with ClusterNode — you don't need to change
 * anything to make these two pass.
 *
 * isolatePair() is intentionally left without an enforced test, since its
 * design is open-ended (see the class javadoc) — describe your approach in
 * your write-up instead. A disabled placeholder test is included below so
 * you can enable and adapt it once you've decided on a design, but it is
 * not required for the automated suite to pass.
 */
class PartitionSimulatorTest {

    private final PartitionSimulator simulator = new PartitionSimulator();

    @Test
    void isolateNodeMarksNodeUnreachable() {
        ClusterNode node = new ClusterNode("node-1");
        simulator.isolateNode(node);
        assertEquals(NodeStatus.UNREACHABLE, node.getStatus());
    }

    @Test
    void healNodeMarksNodeHealthyAgain() {
        ClusterNode node = new ClusterNode("node-1");
        simulator.isolateNode(node);
        simulator.healNode(node);
        assertEquals(NodeStatus.HEALTHY, node.getStatus());
    }

    @Test
    @Disabled("Enable once you've implemented isolatePair() with your own design — see PartitionSimulator javadoc")
    void isolatePairExampleAdaptToYourDesign() {
        ClusterNode nodeA = new ClusterNode("node-1");
        ClusterNode nodeB = new ClusterNode("node-2");
        ClusterNode nodeC = new ClusterNode("node-3");

        simulator.isolatePair(nodeA, nodeB);

        // Example expectation if you go with a per-peer block-list design:
        // nodeA and nodeC should still be able to talk to each other even
        // though nodeA and nodeB cannot. Adapt this to however you expose
        // reachability-between-two-specific-nodes in your implementation.
    }
}
