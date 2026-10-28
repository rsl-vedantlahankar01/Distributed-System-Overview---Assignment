package com.hld.kvstore.node;

import java.util.Optional;

/**
 * Abstraction for anything that can act as a cluster participant.
 *
 * Implemented by AbstractNode (which provides shared bookkeeping) and, in
 * turn, by ClusterNode (the concrete node used in the demo). Keeping this
 * as an interface means Cluster / ReplicationManager never need to know
 * about the concrete class — only about this contract.
 */
public interface Node {

    String getId();

    NodeStatus getStatus();

    void setStatus(NodeStatus status);

    void put(String key, String value);

    Optional<String> get(String key);

    void delete(String key);
}
