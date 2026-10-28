package com.hld.kvstore.consistency;

import com.hld.kvstore.node.ClusterNode;
import com.hld.kvstore.node.NodeStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provided test suite — you do not need to write or modify these tests.
 * Just implement APConsistencyStrategy per the contract in its javadoc
 * until these pass.
 */
class APConsistencyStrategyTest {

    private final APConsistencyStrategy strategy = new APConsistencyStrategy();

    @Test
    void readReturnsPreferredNodeValueWhenReachable() {
        ClusterNode preferred = new ClusterNode("node-1");
        ClusterNode fallback = new ClusterNode("node-2");
        preferred.put("key-1", "fresh-value");
        fallback.put("key-1", "stale-value");

        Optional<String> result = strategy.handleRead("key-1", preferred, fallback);

        assertEquals(Optional.of("fresh-value"), result);
    }

    @Test
    void readFallsBackToReplicaWhenPreferredNodeUnreachable() {
        ClusterNode preferred = new ClusterNode("node-1");
        ClusterNode fallback = new ClusterNode("node-2");
        fallback.put("key-1", "stale-value");
        preferred.setStatus(NodeStatus.UNREACHABLE);

        // AP mode must NOT throw — it should serve the (possibly stale)
        // value from the fallback replica instead.
        Optional<String> result = strategy.handleRead("key-1", preferred, fallback);

        assertEquals(Optional.of("stale-value"), result);
    }

    @Test
    void writeSucceedsWhenPreferredNodeReachable() {
        ClusterNode preferred = new ClusterNode("node-1");

        strategy.handleWrite("key-1", "value-1", preferred);

        assertEquals(Optional.of("value-1"), preferred.get("key-1"));
    }

    @Test
    void writeDoesNotThrowWhenPreferredNodeUnreachable() {
        ClusterNode preferred = new ClusterNode("node-1");
        preferred.setStatus(NodeStatus.UNREACHABLE);

        // AP mode must degrade gracefully rather than propagate the
        // failure back to the caller the way CP mode does.
        assertDoesNotThrow(() -> strategy.handleWrite("key-1", "value-1", preferred));
    }
}
