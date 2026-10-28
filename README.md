# Distributed KV Store — Java Template

This repository is a Maven starter for a distributed key-value store.
Only the four required TODOs are needed for the assignment. Everything
else is already implemented for you.

## Required assignment tasks

1. `ring/ConsistentHashingStrategy`
   - Implement the hash ring and virtual nodes.
2. `node/ClusterNode`
   - Implement the three one-line delegations to `store`.
3. `replication/ReplicationManager`
   - Implement only the `SYNCHRONOUS` branch of `replicate()`.
4. `consistency/APConsistencyStrategy`
   - Implement fallback-to-replica read/write logic.

## Tests

Each required class already has a matching JUnit test class:

- `ConsistentHashingStrategyTest`
- `HashingStrategyComparisonTest`
- `ClusterNodeTest`
- `ReplicationManagerTest` (sync-only tests)
- `APConsistencyStrategyTest`

## Build

From the project root:

```bash
cd /Users/ishant.bisen/Downloads/kv-store-template
mvn compile
```

Expected result: `BUILD SUCCESS`.

## Notes

Do not modify unnecessary files or implement stretch goals unless you
have extra time. The assignment only requires the four items above.

The supported tests are currently disabled by class-level
`@Disabled` annotations. Trainees should re-enable them only after
completing the four required TODO files.
