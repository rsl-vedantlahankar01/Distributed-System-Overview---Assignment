package com.hld.kvstore.ring;

import java.util.List;

/**
 * Strategy interface for mapping a key to the node that owns it.
 *
 * You will provide TWO implementations of this interface:
 *   1. NaiveHashingStrategy   -> hash(key) % N
 *   2. ConsistentHashingStrategy -> hash ring + virtual nodes
 *
 * This is the Strategy design pattern: HashRing (or Cluster) depends only
 * on this interface, so you can swap strategies at runtime and directly
 * compare their rebalancing behavior — that comparison is a required
 * deliverable (see assignment §3.2).
 */
public interface HashingStrategy {

    /**
     * Register a physical node with this strategy (e.g., add it to the ring,
     * or add it to the list used by hash % N).
     */
    void addNode(String nodeId);

    /**
     * Remove a physical node from this strategy.
     */
    void removeNode(String nodeId);

    /**
     * Given a key, return the id of the node that owns it.
     *
     * @throws IllegalStateException if no nodes have been registered
     */
    String getOwnerNode(String key);

    /**
     * Return the current list of registered node ids, in a stable order.
     * Used by tests to inspect distribution.
     */
    List<String> getNodes();
}
