package com.hld.kvstore.cluster;

/**
 * Wire everything together and run the demos.
 *
 * ONE-DAY SCOPE — only 2 demos are required. The other 3 are stretch
 * goals; implement them only if you finish early.
 *
 * REQUIRED:
 *   1. demoRingDistribution()
 *        - Build a cluster with 4 nodes using ConsistentHashingStrategy.
 *        - Insert 1,000 random keys directly via
 *          hashingStrategy.getOwnerNode(...) and count how many land on
 *          each node.
 *        - Add a 5th node, re-run, and report % of keys that moved.
 *        - Repeat with NaiveHashingStrategy (already implemented for you)
 *          and print both results side by side. (Assignment §3.2)
 *
 *   2. demoCapBehavior()
 *        - Build the cluster in CP mode (CPConsistencyStrategy — already
 *          implemented), write a key, use PartitionSimulator#isolateNode
 *          to take down the key's leader, then attempt a read and print
 *          what happens.
 *        - Rebuild in AP mode (your APConsistencyStrategy) and repeat,
 *          printing the different (fallback) behavior. (Assignment §3.4)
 *
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("== Distributed KV Store Demo ==");

        // Required:
        // demoRingDistribution();
        // demoCapBehavior();

        // Stretch goals (optional):
        // demoReplicationLatency();
        // demoReplicationLagCaseStudy();
        // demoHotPartitionCaseStudy();
    }
}
