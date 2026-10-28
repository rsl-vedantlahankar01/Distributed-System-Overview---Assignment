package com.hld.kvstore.ring;

import java.util.ArrayList;
import java.util.List;

/**
 * PROVIDED — fully implemented, you do not need to touch this class.
 *
 * This is the "bad" approach described in the reading (§ "The Problem
 * With Naive Hashing"): owner = nodeList.get(hash(key) % nodeList.size()).
 * It's given to you already working so you can see, via
 * HashingStrategyComparisonTest, how much worse it rebalances compared to
 * the ConsistentHashingStrategy you're about to implement — that contrast
 * is the whole point, and you don't need to write this half yourself to
 * see it.
 */
public class NaiveHashingStrategy implements HashingStrategy {

    private final List<String> nodes = new ArrayList<>();

    @Override
    public void addNode(String nodeId) {
        if (!nodes.contains(nodeId)) {
            nodes.add(nodeId);
        }
    }

    @Override
    public void removeNode(String nodeId) {
        nodes.remove(nodeId);
    }

    @Override
    public String getOwnerNode(String key) {
        if (nodes.isEmpty()) {
            throw new IllegalStateException("No nodes registered");
        }
        int index = Math.floorMod(key.hashCode(), nodes.size());
        return nodes.get(index);
    }

    @Override
    public List<String> getNodes() {
        return List.copyOf(nodes);
    }
}
