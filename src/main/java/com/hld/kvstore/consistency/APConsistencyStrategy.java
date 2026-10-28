package com.hld.kvstore.consistency;

import com.hld.kvstore.node.ClusterNode;
import com.hld.kvstore.node.NodeUnreachableException;

import java.util.Optional;

/**
 * TODO (Assignment §3.4 — AP mode). This is one of the 4 required TODOs
 * for the one-day scope. Read CPConsistencyStrategy first (it's fully
 * implemented) — AP is nearly identical, just with a fallback added.
 *
 * Why this matters:
 * AP (Availability + Partition tolerance) favors availability over
 *     strict consistency during partial failures.
 * When the preferred node is down, the system should still return a
 *     result if a secondary replica can answer, even if that data may be
 *     stale.
 * For writes, AP mode should accept the write rather than failing the
 *     client when the preferred node is unreachable.
 *
 * "The node always answers using whatever local data it has, even if it
 * may be stale, and reconciles once the partition heals."
 *
 * Contract enforced by APConsistencyStrategyTest — implement exactly this:
 * handleRead: try preferredNode.get(key) first. If it throws
 *     NodeUnreachableException, catch it and return fallbackNode.get(key)
 *     instead — even if that data might be stale. Do not let the
 *     exception propagate out of this method.
 * handleWrite: try preferredNode.put(key, value). If it throws
 *     NodeUnreachableException, catch it (do not rethrow) — print/log
 *     that the write was accepted-but-degraded (e.g., "write to {key}
 *     buffered, leader unreachable"), and return normally. The interface
 *     does not give you a fallback node to write to here, so there is
 *     nothing further to persist — the point being tested is simply that
 *     an AP write never throws back to the caller the way a CP write does.
 *
 * TRAINEE ACTION: implement the fallback read and degraded-write behavior.
 * `APConsistencyStrategyTest` only passes when reads fall back on
 * unreachable preferred nodes and writes never propagate that failure.
 */
public class APConsistencyStrategy implements ConsistencyStrategy {

    @Override
    public Optional<String> handleRead(String key, ClusterNode preferredNode, ClusterNode fallbackNode) {
        try {
            return preferredNode.get(key);
        } catch (NodeUnreachableException e) {
            return fallbackNode.get(key);
        }
    }

    @Override
    public void handleWrite(String key, String value, ClusterNode preferredNode) {
        try {
            preferredNode.put(key, value);
        } catch (NodeUnreachableException e) {
            System.out.println("write to " + key + " buffered, leader unreachable");
        }
    }
}