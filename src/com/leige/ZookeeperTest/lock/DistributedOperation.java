package com.leige.ZookeeperTest.lock;

@FunctionalInterface
public interface DistributedOperation<T> {
    T execute() throws DistributedOperationException;
}
