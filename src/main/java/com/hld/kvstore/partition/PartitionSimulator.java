package com.hld.kvstore.partition;

import com.hld.kvstore.node.ClusterNode;
import com.hld.kvstore.node.NodeStatus;

/**
 * Provided for you (mostly implemented).
 *
 * A deliberately simple partition simulator: it does not model real
 * network topology, it just flips a node's status to UNREACHABLE so that
 * AbstractNode.assertReachable() starts throwing NodeUnreachableException
 * for that node. This is enough to exercise your ConsistencyStrategy
 * implementations without any real networking.
 *
 * TODO: the one thing left for you is isolatePair(), which should model a
 * one-directional or two-node partition rather than taking a node down
 * entirely — see the hint below.
 */
public class PartitionSimulator {

    /** Takes a node down entirely — every request to it will fail. */
    public void isolateNode(ClusterNode node) {
        node.setStatus(NodeStatus.UNREACHABLE);
    }

    /** Restores a node after a simulated partition heals. */
    public void healNode(ClusterNode node) {
        node.setStatus(NodeStatus.HEALTHY);
    }

    /**
     * TODO: Simulate a partition between exactly two nodes (e.g., a leader
     * and one follower) rather than taking either node down for ALL
     * traffic. This is closer to a real network partition, where a node
     * is unreachable from some peers but not others.
     *
     * A simple way to do this without building real networking: extend
     * ClusterNode/AbstractNode with a per-peer block-list (e.g.,
     * Set<String> blockedPeerIds) that assertReachable() consults when it
     * knows which peer is calling. Document your approach in the README —
     * there's more than one reasonable design here.
     */
    public void isolatePair(ClusterNode nodeA, ClusterNode nodeB) {
        throw new UnsupportedOperationException("TODO: implement isolatePair");
    }
}
