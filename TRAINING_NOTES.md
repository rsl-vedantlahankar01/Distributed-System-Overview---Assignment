# Trainee Assignment Guide

This repository is intentionally left with the four required TODO stubs.
The matching tests are currently disabled with class-level `@Disabled`
annotations so that a trainee can complete the key implementation tasks first.

## Required TODO files

1. `src/main/java/com/hld/kvstore/ring/ConsistentHashingStrategy.java`
   - Implement the consistent hash ring using a `SortedMap<Long, String>`.
   - Support virtual nodes and clockwise lookup for key ownership.
   - Validate against `ConsistentHashingStrategyTest` and
     `HashingStrategyComparisonTest`.

2. `src/main/java/com/hld/kvstore/node/ClusterNode.java`
   - Implement `doPut`, `doGet`, and `doDelete` by delegating to `store`.
   - Validate against `ClusterNodeTest`.

3. `src/main/java/com/hld/kvstore/replication/ReplicationManager.java`
   - Implement only the `SYNCHRONOUS` branch in `replicate()`.
   - Each follower write must complete before `replicate()` returns.
   - Validate against `ReplicationManagerTest`.

4. `src/main/java/com/hld/kvstore/consistency/APConsistencyStrategy.java`
   - Implement fallback read behavior when `preferredNode` is unreachable.
   - Implement degraded write handling without throwing the exception.
   - Validate against `APConsistencyStrategyTest`.

## Test status

The following test classes are currently disabled:

- `ClusterTest`
- `APConsistencyStrategyTest`
- `CPConsistencyStrategyTest`
- `ClusterNodeTest`
- `PartitionSimulatorTest`
- `ReplicationManagerTest`
- `ConsistentHashingStrategyTest`
- `HashingStrategyComparisonTest`
- `NaiveHashingStrategyTest`

To run tests after implementation, remove the `@Disabled(...)`
annotations from the relevant test classes.

## Build commands

From the project root:

```bash
mvn compile
mvn test
```

Expected result after task completion: `BUILD SUCCESS`.
