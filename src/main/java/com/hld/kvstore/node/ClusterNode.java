package com.hld.kvstore.node;

import com.hld.kvstore.store.InMemoryStore;
import com.hld.kvstore.store.KeyValueStore;

import java.util.Optional;

/**
 * TODO — one of the 4 required TODOs for the one-day scope, and the
 * easiest one: three one-line delegations to `store`.
 *
 * The concrete node used throughout the cluster.
 *
 * Why this matters:
 * `ClusterNode` keeps node lifecycle and status handling separate from
 *     actual key/value storage.
 * The `AbstractNode` base class manages node state and the template
 *     method flow, while `ClusterNode` forwards storage operations to the
 *     underlying `KeyValueStore` implementation.
 * If these methods are not delegated, the cluster will not actually
 *     persist or retrieve any stored values.
 *
 * This class demonstrates composition + inheritance together:
 * it EXTENDS AbstractNode (inherits id/status/template-method logic)
 * it HAS-A KeyValueStore (composition — delegates actual storage)
 *
 * You just need to wire the doPut/doGet/doDelete hooks to the underlying
 * store. The InMemoryStore implementation is already done for you.
 *
 * TRAINEE ACTION: implement the three one-line delegations. This file is
 * validated by `ClusterNodeTest` only.
 */
public class ClusterNode extends AbstractNode {

    private final KeyValueStore store;

    public ClusterNode(String id) {
        super(id);
        this.store = new InMemoryStore();
    }

    @Override
    protected void doPut(String key, String value) {
        store.put(key, value);
    }

    @Override
    protected Optional<String> doGet(String key) {
        return store.get(key);
    }

    @Override
    protected void doDelete(String key) {
        store.delete(key);
    }

    /**
     * Exposed so tests / demos can print how many keys ended up on this
     * node (used for the ring-distribution report in §3.2).
     */
    public int size() {
        return store.size();
    }
}