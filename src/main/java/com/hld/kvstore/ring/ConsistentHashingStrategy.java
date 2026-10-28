package com.hld.kvstore.ring;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * TODO — one of the 4 required TODOs for the one-day scope, and the core
 * concept for the whole assignment. This is the part that decides which
 * physical node owns each key in the cluster.
 *
 * Why this matters:
 * Distributed key-value stores need to assign keys to nodes in a way
 *     that minimizes data movement when nodes join or leave.
 * Consistent hashing achieves that by placing nodes and keys on a
 *     logical ring and assigning each key to the next node clockwise.
 * Virtual nodes spread each physical node across many ring positions,
 *     which avoids hot spots and makes the distribution smoother.
 *
 * Assignment §3.2 — "How Consistent Hashing Works" + "Virtual Nodes":
 * Both node ids and keys are hashed into the same numeric space.
 * A key belongs to the first node found walking clockwise from the
 *     key's position on the ring (i.e., the next-highest hash value,
 *     wrapping around to the smallest if none is found).
 * Each physical node gets VIRTUAL_NODES_PER_NODE positions on the ring
 *     (e.g., "node-1#0", "node-1#1", ... "node-1#99"), so load is spread
 *     evenly and rebalancing on add/remove is spread across many nodes
 *     instead of concentrated on one neighbor.
 *
 * A SortedMap<Long, String> (ring position -> physical node id) is a good
 * fit here — TreeMap gives you tailMap()/firstKey() for the "clockwise"
 * lookup almost for free. That's already imported for you.
 *
 * TRAINEE ACTION: implement the hash ring and virtual nodes so keys map
 * consistently to the correct owner. This file is validated by
 * `ConsistentHashingStrategyTest` and `HashingStrategyComparisonTest`.
 * The latter only passes when your ring moves a much smaller fraction of
 * keys than the naive strategy after a node add.
 */
public class ConsistentHashingStrategy implements HashingStrategy {

    private static final int VIRTUAL_NODES_PER_NODE = 100;

    // ring position (hash value) -> physical node id
    private final SortedMap<Long, String> ring = new TreeMap<>();
    private final List<String> physicalNodes = new ArrayList<>();

    @Override
    public void addNode(String nodeId) {
        if (!physicalNodes.contains(nodeId)) {
            physicalNodes.add(nodeId);
        }
        for (int i = 0; i < VIRTUAL_NODES_PER_NODE; i++) {
            long hashValue = hash(nodeId + "#" + i);
            ring.put(hashValue, nodeId);
        }
    }

    @Override
    public void removeNode(String nodeId) {
        physicalNodes.remove(nodeId);
        for (int i = 0; i < VIRTUAL_NODES_PER_NODE; i++) {
            long hashValue = hash(nodeId + "#" + i);
            ring.remove(hashValue);
        }
    }

    @Override
    public String getOwnerNode(String key) {
        if (ring.isEmpty()) {
            throw new IllegalStateException("The ring is empty. Cannot determine key owner.");
        }
        long hashValue = hash(key);
        SortedMap<Long, String> tailMap = ring.tailMap(hashValue);
        long nodeHash = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();
        return ring.get(nodeHash);
    }

    @Override
    public List<String> getNodes() {
        return List.copyOf(physicalNodes);
    }

    /**
     * Provided for you: a stable hash function mapping any string to a
     * non-negative long. Use this in addNode/getOwnerNode so your results
     * are reproducible and comparable with classmates'.
     */
    protected long hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = digest.digest(input.getBytes());
            // Use the first 8 bytes of the SHA-1 digest as a long.
            long value = 0;
            for (int i = 0; i < 8; i++) {
                value = (value << 8) | (bytes[i] & 0xff);
            }
            return Math.abs(value);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-1 not available", e);
        }
    }
}