package com.hld.kvstore.node;

/**
 * Provided for you. Represents whether a node is currently reachable.
 * Used by PartitionSimulator to flip nodes to UNREACHABLE and simulate
 * a network partition without any real networking involved.
 */
public enum NodeStatus {
    HEALTHY,
    UNREACHABLE
}
