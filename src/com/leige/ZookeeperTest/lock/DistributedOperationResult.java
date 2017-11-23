package com.leige.ZookeeperTest.lock;

public class DistributedOperationResult<T> {

    public final boolean timedOut;
    public final T result;

    public DistributedOperationResult(boolean timedOut, T result) {
        this.timedOut = timedOut;
        this.result = result;
    }
}
