package com.hld.kvstore.node;

import java.util.Optional;

/**
 * Provided for you (mostly implemented).
 *
 * Holds the fields and status-checking logic every node needs, and uses
 * the Template Method pattern: put()/get()/delete() are final and always
 * check node status first, then delegate the actual storage work to
 * doPut()/doGet()/doDelete(), which concrete subclasses (ClusterNode)
 * implement.
 *
 * This is the inheritance + encapsulation piece of the assignment: the
 * "is this node reachable?" check lives in exactly one place, and every
 * subclass automatically gets it for free.
 */
public abstract class AbstractNode implements Node {

    private final String id;
    private NodeStatus status;

    protected AbstractNode(String id) {
        this.id = id;
        this.status = NodeStatus.HEALTHY;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public NodeStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(NodeStatus status) {
        this.status = status;
    }

    @Override
    public final void put(String key, String value) {
        assertReachable();
        doPut(key, value);
    }

    @Override
    public final Optional<String> get(String key) {
        assertReachable();
        return doGet(key);
    }

    @Override
    public final void delete(String key) {
        assertReachable();
        doDelete(key);
    }

    private void assertReachable() {
        if (status == NodeStatus.UNREACHABLE) {
            throw new NodeUnreachableException(id);
        }
    }

    // ---- subclasses implement the actual storage behavior ----
    protected abstract void doPut(String key, String value);

    protected abstract Optional<String> doGet(String key);

    protected abstract void doDelete(String key);

    @Override
    public String toString() {
        return "Node{" + id + ", status=" + status + "}";
    }
}
