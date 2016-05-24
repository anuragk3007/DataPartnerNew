package monitor;

import org.apache.log4j.Logger;
import utils.DataPartnerConstants;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * Created by kanchan.chowdhary on
 * 5/23/2016.
 */
public class RequestFinderMonitor extends Thread {
    private static final Logger LOGGER = Logger.getLogger(RequestFinderMonitor.class);

    private final CountDownLatch latch;
    private final BlockingQueue sharedQueue;

    public RequestFinderMonitor(CountDownLatch latch, BlockingQueue sharedQueue) {
        this.latch = latch;
        this.sharedQueue = sharedQueue;
    }

    public void run() {
        try {
            while (latch.getCount() > 0) {
                synchronized (this) {
                    LOGGER.info("Status : Threads Processing - " + Thread.activeCount()+'\t' +
                            "Shared Queue size - " + sharedQueue.size()+'\t' +
                            "Completed Thread: " + (DataPartnerConstants.THREAD_COUNT - latch.getCount()));
                    this.wait(DataPartnerConstants.MONITOR_INTERVAL);
                }
            }
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
    }
}
