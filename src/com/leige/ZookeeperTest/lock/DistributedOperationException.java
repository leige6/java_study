package com.leige.ZookeeperTest.lock;

@SuppressWarnings("unused")
public class DistributedOperationException extends RuntimeException {
    public DistributedOperationException() {
        super();
    }

    public DistributedOperationException(String message) {
        super(message);
    }

    public DistributedOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DistributedOperationException(Throwable cause) {
        super(cause);
    }
}
