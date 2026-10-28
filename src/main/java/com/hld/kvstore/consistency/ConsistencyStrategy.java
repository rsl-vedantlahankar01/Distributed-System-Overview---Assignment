package com.hld.kvstore.consistency;

import com.hld.kvstore.node.ClusterNode;

import java.util.Optional;

/**
 * Strategy interface for how the cluster behaves when a node it needs to
 * reach is unreachable (i.e., during a simulated partition).
 *
 * This is the core of the CAP demo (Assignment §3.4): the Cluster class
 * calls into whichever ConsistencyStrategy it was configured with, and the
 * two implementations (CP/AP) diverge in behavior from that single point.
 * Swapping strategies at runtime is what lets you demonstrate CP vs AP
 * against the exact same underlying cluster state.
 */
public interface ConsistencyStrategy {

    /**
     * Handle a read that should come from `preferredNode`, but a partition
     * may make it unreachable. `fallbackNode` is a replica that may have
     * stale data.
     */
    Optional<String> handleRead(String key, ClusterNode preferredNode, ClusterNode fallbackNode);

    /**
     * Handle a write that should go to `preferredNode` (the leader), but a
     * partition may make it unreachable.
     */
    void handleWrite(String key, String value, ClusterNode preferredNode);
}
