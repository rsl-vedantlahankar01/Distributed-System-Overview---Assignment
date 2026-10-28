package com.hld.kvstore.store;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A simple thread-safe in-memory implementation of KeyValueStore.
 *
 * This class is provided fully implemented — you do not need to change it.
 * It exists so you can focus on the ring / replication / consistency logic,
 * which is the point of this assignment.
 */
public class InMemoryStore implements KeyValueStore {

    private final Map<String, String> data = new ConcurrentHashMap<>();

    @Override
    public void put(String key, String value) {
        data.put(key, value);
    }

    @Override
    public Optional<String> get(String key) {
        return Optional.ofNullable(data.get(key));
    }

    @Override
    public void delete(String key) {
        data.remove(key);
    }

    @Override
    public boolean containsKey(String key) {
        return data.containsKey(key);
    }

    @Override
    public int size() {
        return data.size();
    }
}
