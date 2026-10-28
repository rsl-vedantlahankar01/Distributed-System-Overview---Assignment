package com.hld.kvstore.store;

import java.util.Optional;

/**
 * Abstraction over a node's local storage engine.
 *
 * This is deliberately an interface (not a class) so that InMemoryStore
 * could later be swapped for, e.g., a DiskBackedStore, without any other
 * class in the system needing to change. This is the "program to an
 * interface, not an implementation" OOP principle in action.
 */
public interface KeyValueStore {

    void put(String key, String value);

    Optional<String> get(String key);

    void delete(String key);

    boolean containsKey(String key);

    int size();
}
