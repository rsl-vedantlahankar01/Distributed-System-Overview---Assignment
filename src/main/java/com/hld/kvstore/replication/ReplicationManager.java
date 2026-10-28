package com.hld.kvstore.replication;

import com.hld.kvstore.node.ClusterNode;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReplicationManager {

    private final ExecutorService asyncExecutor = Executors.newCachedThreadPool();
    private final List<ReplicationLogEntry> log = new CopyOnWriteArrayList<>();

    public void replicate(String key, String value, List<ClusterNode> followers, ReplicationMode mode) {
        if (mode == ReplicationMode.SYNCHRONOUS) {
            for (ClusterNode follower : followers) {
                long start = System.currentTimeMillis();
                follower.put(key, value);
                long elapsed = System.currentTimeMillis() - start;
                // Corrected: added 'key' as the first parameter
                log.add(new ReplicationLogEntry(key, follower.getId(), mode, elapsed));
            }
        } else {
            for (ClusterNode follower : followers) {
                asyncExecutor.submit(() -> {
                    long start = System.currentTimeMillis();
                    follower.put(key, value);
                    long elapsed = System.currentTimeMillis() - start;
                    // Corrected: added 'key' as the first parameter
                    log.add(new ReplicationLogEntry(key, follower.getId(), mode, elapsed));
                });
            }
        }
    }

    public List<ReplicationLogEntry> getLog() {
        return List.copyOf(log);
    }

    public void shutdown() {
        asyncExecutor.shutdown();
    }
}