package com.leige.ZookeeperTest.lock;


import com.leige.ZookeeperTest.util.ConnectionHelper;
import com.leige.ZookeeperTest.util.RandomAmountOfWork;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Worker uses the {@link BlockingWriteLock}.
 */
public class WorkerUsingBlockingWriteLock {

    private static final Logger LOG = LoggerFactory.getLogger(WorkerUsingBlockingWriteLock.class);

    private WorkerUsingBlockingWriteLock() {
    }

    public static void main(String[] args) throws Exception {
        /*String hosts = args[0];
        String path = args[1];
        String myName = args[2];*/

        String hosts = "127.0.0.1";
        String path = "/distrubate_lock";
        String myName = "client1";
        ConnectionHelper connectionHelper = new ConnectionHelper();
        ZooKeeper zooKeeper = connectionHelper.connect(hosts);
        BlockingWriteLock lock = new BlockingWriteLock(myName, zooKeeper, path, ZooDefs.Ids.OPEN_ACL_UNSAFE);

        LOG.info("{} is attempting to obtain lock on {}...", myName, path);
        //System.out.println(myName+" is attempting to obtain lock on "+path+".....");
        lock.lock();
       // System.out.println(myName+" has obtained lock on "+path);
        LOG.info("{} has obtained lock on {}", myName, path);

        doSomeWork(myName);
        //System.out.println(myName+"is done doing work, releasing lock on "+path);
        LOG.info("{} is done doing work, releasing lock on {}", myName, path);

        lock.unlock();  // Does not need to be in a finally. Why?  (hint: we're in a main method)
    }

    private static void doSomeWork(String name) {
        int seconds = new RandomAmountOfWork().timeItWillTake();
        long workTimeMillis = seconds * 1000L;
        LOG.info("{} is doing some work for {} seconds", name, seconds);
        //System.out.println(name+" is doing some work for "+seconds+" seconds");
        try {
            Thread.sleep(workTimeMillis);
        } catch (InterruptedException ex) {
            LOG.error("Oops. Interrupted.", ex);
            Thread.currentThread().interrupt();
        }
    }
}
