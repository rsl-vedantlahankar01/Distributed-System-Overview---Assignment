package com.hld.kvstore.replication;

import java.time.Instant;

/**
 * Provided for you. Immutable record of a single replication event, used to
 * produce the "Replication logs" deliverable (Assignment §4.3): sample logs
 * from a sync run and an async run showing the latency difference.
 */
public final class ReplicationLogEntry {

    private final String key;
    private final String followerId;
    private final ReplicationMode mode;
    private final Instant timestamp;
    private final long latencyMillis;

    public ReplicationLogEntry(String key, String followerId, ReplicationMode mode, long latencyMillis) {
        this.key = key;
        this.followerId = followerId;
        this.mode = mode;
        this.timestamp = Instant.now();
        this.latencyMillis = latencyMillis;
    }

    @Override
    public String toString() {
        return String.format("[%s] key=%s follower=%s mode=%s latencyMs=%d",
                timestamp, key, followerId, mode, latencyMillis);
    }
}
