package com.hld.kvstore.consistency;

import com.hld.kvstore.node.ClusterNode;
import com.hld.kvstore.node.NodeStatus;
import com.hld.kvstore.node.NodeUnreachableException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provided test suite — you do not need to write or modify these tests.
 * Just implement CPConsistencyStrategy per the contract in its javadoc
 * until these pass.
 */
class CPConsistencyStrategyTest {

    private final CPConsistencyStrategy strategy = new CPConsistencyStrategy();

    @Test
    void readReturnsValueWhenPreferredNodeIsReachable() {
        ClusterNode preferred = new ClusterNode("node-1");
        ClusterNode fallback = new ClusterNode("node-2");
        preferred.put("key-1", "fresh-value");

        Optional<String> result = strategy.handleRead("key-1", preferred, fallback);

        assertEquals(Optional.of("fresh-value"), result);
    }

    @Test
    void readThrowsWhenPreferredNodeIsUnreachable() {
        ClusterNode preferred = new ClusterNode("node-1");
        ClusterNode fallback = new ClusterNode("node-2");
        fallback.put("key-1", "stale-value");
        preferred.setStatus(NodeStatus.UNREACHABLE);

        // CP mode must NOT fall back to a possibly-stale replica — it
        // should propagate the failure instead.
        assertThrows(NodeUnreachableException.class,
                () -> strategy.handleRead("key-1", preferred, fallback));
    }

    @Test
    void writeSucceedsWhenPreferredNodeIsReachable() {
        ClusterNode preferred = new ClusterNode("node-1");

        strategy.handleWrite("key-1", "value-1", preferred);

        assertEquals(Optional.of("value-1"), preferred.get("key-1"));
    }

    @Test
    void writeThrowsWhenPreferredNodeIsUnreachable() {
        ClusterNode preferred = new ClusterNode("node-1");
        preferred.setStatus(NodeStatus.UNREACHABLE);

        assertThrows(NodeUnreachableException.class,
                () -> strategy.handleWrite("key-1", "value-1", preferred));
    }
}
