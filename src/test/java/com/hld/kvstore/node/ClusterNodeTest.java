package com.hld.kvstore.node;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provided test suite — you do not need to write or modify these tests.
 * Just implement ClusterNode's doPut/doGet/doDelete until these pass.
 */
class ClusterNodeTest {

    @Test
    void putThenGetReturnsTheStoredValue() {
        ClusterNode node = new ClusterNode("node-1");
        node.put("key-1", "value-1");
        assertEquals(Optional.of("value-1"), node.get("key-1"));
    }

    @Test
    void getOnMissingKeyReturnsEmpty() {
        ClusterNode node = new ClusterNode("node-1");
        assertEquals(Optional.empty(), node.get("does-not-exist"));
    }

    @Test
    void deleteRemovesTheKey() {
        ClusterNode node = new ClusterNode("node-1");
        node.put("key-1", "value-1");
        node.delete("key-1");
        assertEquals(Optional.empty(), node.get("key-1"));
    }

    @Test
    void putOverwritesExistingValue() {
        ClusterNode node = new ClusterNode("node-1");
        node.put("key-1", "value-1");
        node.put("key-1", "value-2");
        assertEquals(Optional.of("value-2"), node.get("key-1"));
    }

    @Test
    void sizeReflectsNumberOfStoredKeys() {
        ClusterNode node = new ClusterNode("node-1");
        node.put("a", "1");
        node.put("b", "2");
        assertEquals(2, node.size());
    }

    @Test
    void unreachableNodeThrowsOnPut() {
        ClusterNode node = new ClusterNode("node-1");
        node.setStatus(NodeStatus.UNREACHABLE);
        assertThrows(NodeUnreachableException.class, () -> node.put("key-1", "value-1"));
    }

    @Test
    void unreachableNodeThrowsOnGet() {
        ClusterNode node = new ClusterNode("node-1");
        node.setStatus(NodeStatus.UNREACHABLE);
        assertThrows(NodeUnreachableException.class, () -> node.get("key-1"));
    }

    @Test
    void unreachableNodeThrowsOnDelete() {
        ClusterNode node = new ClusterNode("node-1");
        node.setStatus(NodeStatus.UNREACHABLE);
        assertThrows(NodeUnreachableException.class, () -> node.delete("key-1"));
    }

    @Test
    void healedNodeWorksAgainAfterBeingUnreachable() {
        ClusterNode node = new ClusterNode("node-1");
        node.setStatus(NodeStatus.UNREACHABLE);
        node.setStatus(NodeStatus.HEALTHY);
        node.put("key-1", "value-1");
        assertEquals(Optional.of("value-1"), node.get("key-1"));
    }
}
