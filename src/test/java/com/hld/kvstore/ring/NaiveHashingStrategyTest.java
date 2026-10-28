package com.hld.kvstore.ring;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provided test suite — you do not need to write or modify these tests.
 * Just implement NaiveHashingStrategy until these pass.
 */
class NaiveHashingStrategyTest {

    @Test
    void ownerNodeIsStableAcrossRepeatedCalls() {
        NaiveHashingStrategy strategy = new NaiveHashingStrategy();
        strategy.addNode("node-1");
        strategy.addNode("node-2");
        strategy.addNode("node-3");

        String key = "user:1234";
        assertEquals(strategy.getOwnerNode(key), strategy.getOwnerNode(key));
    }

    @Test
    void throwsWhenNoNodesRegistered() {
        NaiveHashingStrategy strategy = new NaiveHashingStrategy();
        assertThrows(IllegalStateException.class, () -> strategy.getOwnerNode("any-key"));
    }

    @Test
    void addingANodeMovesMostKeys() {
        NaiveHashingStrategy strategy = new NaiveHashingStrategy();
        for (int i = 1; i <= 5; i++) {
            strategy.addNode("node-" + i);
        }

        int totalKeys = 10_000;
        Map<String, String> before = new HashMap<>();
        for (int i = 0; i < totalKeys; i++) {
            String key = UUID.randomUUID().toString();
            before.put(key, strategy.getOwnerNode(key));
        }

        strategy.addNode("node-6");

        int moved = 0;
        for (Map.Entry<String, String> entry : before.entrySet()) {
            if (!entry.getValue().equals(strategy.getOwnerNode(entry.getKey()))) {
                moved++;
            }
        }

        double movedFraction = moved / (double) totalKeys;
        System.out.printf("[Naive] Fraction of keys moved after adding a 6th node: %.2f%%%n", movedFraction * 100);

        // This is the whole point of §3.2: naive hash % N should move the
        // large majority of keys when N changes. If this fails, your
        // implementation is accidentally doing something smarter than
        // plain modulo — that's not what this class is supposed to do.
        assertTrue(movedFraction > 0.5,
                "Naive hashing (hash % N) should move the majority of keys when N changes");
    }
}
