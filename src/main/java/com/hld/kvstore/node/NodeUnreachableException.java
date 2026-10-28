package com.hld.kvstore.node;

/**
 * Provided for you. Thrown when a request is made to a node whose status
 * has been flipped to UNREACHABLE by the PartitionSimulator. Your
 * ConsistencyStrategy implementations (CP/AP) should catch this and decide
 * what to do — that decision is the whole point of the CAP demo.
 */
public class NodeUnreachableException extends RuntimeException {
    public NodeUnreachableException(String nodeId) {
        super("Node " + nodeId + " is currently unreachable (simulated partition)");
    }
}
