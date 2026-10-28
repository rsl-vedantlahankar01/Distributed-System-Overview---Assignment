package com.hld.kvstore.replication;

import com.hld.kvstore.node.ClusterNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provided test suite — you do not need to write or modify these tests.
 * Just implement ReplicationManager#replicate until these pass.
 */
class ReplicationManagerTest {

    private final ReplicationManager manager = new ReplicationManager();

    @AfterEach
    void tearDown() {
        manager.shutdown();
    }

    @Test
    void synchronousReplicationWritesToAllFollowersBeforeReturning() {
        ClusterNode follower1 = new ClusterNode("node-2");
        ClusterNode follower2 = new ClusterNode("node-3");

        manager.replicate("key-1", "value-1", List.of(follower1, follower2), ReplicationMode.SYNCHRONOUS);

        // Because replicate() is synchronous, both followers must already
        // have the value by the time this line runs — no waiting/polling.
        assertEquals(Optional.of("value-1"), follower1.get("key-1"));
        assertEquals(Optional.of("value-1"), follower2.get("key-1"));
    }

    @Test
    void synchronousReplicationLogsOneEntryPerFollower() {
        ClusterNode follower1 = new ClusterNode("node-2");
        ClusterNode follower2 = new ClusterNode("node-3");

        manager.replicate("key-1", "value-1", List.of(follower1, follower2), ReplicationMode.SYNCHRONOUS);

        assertEquals(2, manager.getLog().size());
    }

    @Test
    @Disabled("Stretch goal — enable once you've implemented the ASYNCHRONOUS branch of replicate()")
    void asynchronousReplicationEventuallyWritesToFollowers() throws InterruptedException {
        ClusterNode follower = new ClusterNode("node-2");

        manager.replicate("key-1", "value-1", List.of(follower), ReplicationMode.ASYNCHRONOUS);

        // Poll for up to 2 seconds — async replication should not require
        // the caller to wait, but the write should still land eventually.
        boolean replicated = pollUntilTrue(
                () -> follower.get("key-1").equals(Optional.of("value-1")), 2000);

        assertTrue(replicated, "Expected follower to eventually receive the asynchronously replicated value");
    }

    @Test
    @Disabled("Stretch goal — enable once you've implemented the ASYNCHRONOUS branch of replicate()")
    void asynchronousReplicationEventuallyLogsAnEntry() throws InterruptedException {
        ClusterNode follower = new ClusterNode("node-2");

        manager.replicate("key-1", "value-1", List.of(follower), ReplicationMode.ASYNCHRONOUS);

        boolean logged = pollUntilTrue(() -> !manager.getLog().isEmpty(), 2000);

        assertTrue(logged, "Expected a ReplicationLogEntry to eventually be recorded for the async replication");
    }

    private boolean pollUntilTrue(java.util.function.BooleanSupplier condition, long timeoutMillis)
            throws InterruptedException {
        long deadline = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < deadline) {
            if (condition.getAsBoolean()) {
                return true;
            }
            Thread.sleep(20);
        }
        return condition.getAsBoolean();
    }
}
