package com.hld.kvstore.ring;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Provided test — the direct comparison required by Assignment §3.2. This
 * deliberately does NOT hardcode exact percentages (your hash function
 * choice will shift the numbers slightly); it only asserts the relative
 * relationship that must hold regardless of implementation details:
 * consistent hashing must move meaningfully fewer keys than naive hashing
 * when a node is added.
 */
class HashingStrategyComparisonTest {

    private double fractionMovedOnNodeAdd(HashingStrategy strategy) {
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
        return moved / (double) totalKeys;
    }

    @Test
    void consistentHashingMovesFarFewerKeysThanNaiveHashing() {
        double naiveFraction = fractionMovedOnNodeAdd(new NaiveHashingStrategy());
        double consistentFraction = fractionMovedOnNodeAdd(new ConsistentHashingStrategy());

        System.out.printf("Naive moved: %.2f%% | Consistent moved: %.2f%%%n",
                naiveFraction * 100, consistentFraction * 100);

        assertTrue(consistentFraction < naiveFraction / 2,
                "Consistent hashing should move meaningfully fewer keys than naive hashing "
                        + "(expected consistent fraction to be less than half of naive fraction)");
    }
}
