package com.hld.kvstore.ring;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A starter test — extend this as you implement ConsistentHashingStrategy.
 * These are intentionally loose (checking rough distribution / rebalancing
 * cost) rather than exact values, since your hash function choice will
 * shift the numbers slightly.
 */
class ConsistentHashingStrategyTest {

    @Test
    void ownerNodeIsStableAcrossRepeatedCalls() {
        ConsistentHashingStrategy ring = new ConsistentHashingStrategy();
        ring.addNode("node-1");
        ring.addNode("node-2");
        ring.addNode("node-3");

        String key = "user:1234";
        String owner1 = ring.getOwnerNode(key);
        String owner2 = ring.getOwnerNode(key);

        assertEquals(owner1, owner2, "Same key should always map to the same node while ring is unchanged");
    }

    @Test
    void addingANodeMovesOnlyASmallFractionOfKeys() {
        ConsistentHashingStrategy ring = new ConsistentHashingStrategy();
        ring.addNode("node-1");
        ring.addNode("node-2");
        ring.addNode("node-3");
        ring.addNode("node-4");
        ring.addNode("node-5");

        int totalKeys = 10_000;
        Map<String, String> before = new HashMap<>();
        for (int i = 0; i < totalKeys; i++) {
            String key = UUID.randomUUID().toString();
            before.put(key, ring.getOwnerNode(key));
        }

        ring.addNode("node-6");

        int moved = 0;
        for (Map.Entry<String, String> entry : before.entrySet()) {
            if (!entry.getValue().equals(ring.getOwnerNode(entry.getKey()))) {
                moved++;
            }
        }

        double movedFraction = moved / (double) totalKeys;
        System.out.printf("Fraction of keys moved after adding a 6th node: %.2f%%%n", movedFraction * 100);

        // With 6 nodes, roughly 1/6 of keys (~16.7%) should move ideally.
        // Assert it's well below "almost everything moved" (naive hashing
        // would move close to 100%).
        assertTrue(movedFraction < 0.35, "Consistent hashing should move well under 35% of keys, not ~100%");
    }
}
