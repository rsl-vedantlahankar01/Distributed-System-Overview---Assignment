package com.hld.kvstore.consistency;

import com.hld.kvstore.node.ClusterNode;

import java.util.Optional;

/**
 * PROVIDED — fully implemented, you do not need to touch this class.
 *
 * CP mode: a thin pass-through to the preferred node. It never falls back
 * to a possibly-stale replica — if the preferred node is unreachable,
 * NodeUnreachableException simply propagates up (AbstractNode already
 * throws it; this class just doesn't catch it).
 *
 * Read this class carefully before implementing APConsistencyStrategy —
 * AP is the same shape with one deliberate difference (see its javadoc).
 */
public class CPConsistencyStrategy implements ConsistencyStrategy {

    @Override
    public Optional<String> handleRead(String key, ClusterNode preferredNode, ClusterNode fallbackNode) {
        return preferredNode.get(key);
    }

    @Override
    public void handleWrite(String key, String value, ClusterNode preferredNode) {
        preferredNode.put(key, value);
    }
}
