package com.hld.kvstore.cluster;

import com.hld.kvstore.consistency.ConsistencyStrategy;
import com.hld.kvstore.node.ClusterNode;
import com.hld.kvstore.replication.ReplicationManager;
import com.hld.kvstore.replication.ReplicationMode;
import com.hld.kvstore.ring.HashingStrategy;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * PROVIDED — fully implemented, you do not need to touch this class.
 *
 * This is orchestration/plumbing that composes the pieces you DO implement
 * (HashingStrategy, ClusterNode, ReplicationManager, ConsistencyStrategy).
 * It's given to you so you can spend your time on the 4 required TODOs
 * instead of wiring — but it's worth reading, since it shows how those
 * pieces fit together in a real design.
 */
public class Cluster {

    private final Map<String, ClusterNode> nodesById = new LinkedHashMap<>();
    private final HashingStrategy hashingStrategy;
    private final ReplicationManager replicationManager;
    private final ConsistencyStrategy consistencyStrategy;
    private final ReplicationMode replicationMode;
    private final int replicationFactor;

    public Cluster(HashingStrategy hashingStrategy,
                    ReplicationManager replicationManager,
                    ConsistencyStrategy consistencyStrategy,
                    ReplicationMode replicationMode,
                    int replicationFactor) {
        this.hashingStrategy = hashingStrategy;
        this.replicationManager = replicationManager;
        this.consistencyStrategy = consistencyStrategy;
        this.replicationMode = replicationMode;
        this.replicationFactor = replicationFactor;
    }

    public void addNode(ClusterNode node) {
        nodesById.put(node.getId(), node);
        hashingStrategy.addNode(node.getId());
    }

    public void removeNode(String nodeId) {
        nodesById.remove(nodeId);
        hashingStrategy.removeNode(nodeId);
    }

    public void put(String key, String value) {
        String ownerId = hashingStrategy.getOwnerNode(key);
        ClusterNode owner = nodesById.get(ownerId);
        consistencyStrategy.handleWrite(key, value, owner);

        List<ClusterNode> followers = getFollowers(key);
        if (!followers.isEmpty()) {
            replicationManager.replicate(key, value, followers, replicationMode);
        }
    }

    public Optional<String> get(String key) {
        String ownerId = hashingStrategy.getOwnerNode(key);
        ClusterNode owner = nodesById.get(ownerId);
        List<ClusterNode> followers = getFollowers(key);
        ClusterNode fallback = followers.isEmpty() ? owner : followers.get(0);
        return consistencyStrategy.handleRead(key, owner, fallback);
    }

    public List<ClusterNode> getFollowers(String key) {
        String ownerId = hashingStrategy.getOwnerNode(key);
        List<String> allNodeIds = hashingStrategy.getNodes();

        List<ClusterNode> followers = new ArrayList<>();
        int ownerIndex = allNodeIds.indexOf(ownerId);
        int total = allNodeIds.size();

        for (int i = 1; i <= replicationFactor && i < total; i++) {
            String candidateId = allNodeIds.get((ownerIndex + i) % total);
            if (!candidateId.equals(ownerId)) {
                followers.add(nodesById.get(candidateId));
            }
        }
        return followers;
    }

    public ClusterNode getNode(String nodeId) {
        return nodesById.get(nodeId);
    }

    public Map<String, ClusterNode> getAllNodes() {
        return Map.copyOf(nodesById);
    }
}
