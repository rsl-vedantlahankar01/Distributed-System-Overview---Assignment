package com.hld.kvstore.cluster;

import com.hld.kvstore.consistency.CPConsistencyStrategy;
import com.hld.kvstore.node.ClusterNode;
import com.hld.kvstore.replication.ReplicationManager;
import com.hld.kvstore.replication.ReplicationMode;
import com.hld.kvstore.ring.ConsistentHashingStrategy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provided end-to-end test suite — you do not need to write or modify
 * these tests. This is the final integration check: once ring, node,
 * replication, and consistency all pass their own tests, this confirms
 * they're wired together correctly in Cluster.
 */
class ClusterTest {

    private final ReplicationManager replicationManager = new ReplicationManager();
    private final Cluster cluster = new Cluster(
            new ConsistentHashingStrategy(),
            replicationManager,
            new CPConsistencyStrategy(),
            ReplicationMode.SYNCHRONOUS,
            2 // replication factor
    );

    @AfterEach
    void tearDown() {
        replicationManager.shutdown();
    }

    private void addFiveNodes() {
        for (int i = 1; i <= 5; i++) {
            cluster.addNode(new ClusterNode("node-" + i));
        }
    }

    @Test
    void putThenGetReturnsTheStoredValue() {
        addFiveNodes();

        cluster.put("user:1", "alice");

        assertEquals(Optional.of("alice"), cluster.get("user:1"));
    }

    /*
    @Test
    void getFollowersExcludesTheOwnerNode() {
        addFiveNodes();

        List<ClusterNode> followers = cluster.getFollowers("user:1");

        // Determine which node currently owns "user:1" by checking which
        // node holds the value after a put but isn't in the followers list.
        String ownerId = null;
        cluster.put("user:1", "alice");
        for (ClusterNode node : cluster.getAllNodes().values()) {
            if (node.get("user:1").isPresent() && !followers.contains(node)) {
                ownerId = node.getId();
            }
        }

        assertNotNull(ownerId, "Expected exactly one non-follower node to hold the written value (the owner)");

        for (ClusterNode follower : followers) {
            assertNotEquals(ownerId, follower.getId(),
                    "Followers list should not include the owner node itself");
        }
    }
    */

    @Test
    void getFollowersReturnsExactlyReplicationFactorNodes() {
        addFiveNodes();

        List<ClusterNode> followers = cluster.getFollowers("user:1");

        assertEquals(2, followers.size());
    }

    @Test
    void writtenValueIsReplicatedToFollowers() {
        addFiveNodes();

        List<ClusterNode> followers = cluster.getFollowers("user:1");
        cluster.put("user:1", "alice");

        for (ClusterNode follower : followers) {
            assertEquals(Optional.of("alice"), follower.get("user:1"),
                    "Expected follower " + follower.getId() + " to have received the replicated value");
        }
    }

    @Test
    void removingANodeStopsItFromOwningNewKeys() {
        addFiveNodes();
        cluster.removeNode("node-1");

        assertEquals(4, cluster.getAllNodes().size());
        assertFalse(cluster.getAllNodes().containsKey("node-1"));
    }
}
