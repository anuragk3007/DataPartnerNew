package utils;

import filereader.UrlBehaviorListGenerator;
import filereader.UrlLogFileReader;
import monitor.RequestFinderMonitor;
import org.apache.log4j.Logger;
import service.DataPartnerUpdateServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by kanchan.chowdhary on
 * 5/22/2016.
 */
public class DataPartnerRequestFinder {
    private static final Logger LOGGER = Logger.getLogger(DataPartnerRequestFinder.class);

    private final Date logDate;
    private final BlockingQueue sharedQueue;
    private final UrlLogFileReader urlLogFileReader;
    private final DataPartnerUpdateServiceImpl[] dataPartnerUpdateServices;
    private final RequestFinderMonitor monitor;

    public DataPartnerRequestFinder(Date logDate) {
        this.logDate = (Date) logDate.clone();
        sharedQueue = new LinkedBlockingQueue();
        urlLogFileReader = new UrlLogFileReader(this.logDate, sharedQueue);
        dataPartnerUpdateServices = new DataPartnerUpdateServiceImpl[DataPartnerConstants.THREAD_COUNT];
        CountDownLatch latch = new CountDownLatch(DataPartnerConstants.THREAD_COUNT);
        UrlBehaviorListGenerator urlBehaviorListGenerator = new UrlBehaviorListGenerator(this.logDate);
        for (int index = 0; index < DataPartnerConstants.THREAD_COUNT; index++) {
            dataPartnerUpdateServices[index] = new DataPartnerUpdateServiceImpl(
                    DataPartnerConstants.DATA_PARTNER_REPORT_DATE_FORMAT.format(this.logDate),
                    sharedQueue, latch, urlBehaviorListGenerator);
        }
        monitor = new RequestFinderMonitor(latch, sharedQueue);
    }

    @SuppressWarnings("unchecked")
    public void process() {
        LOGGER.info("Started Processing Data for " + logDate.toString());
        ExecutorService service = Executors.newFixedThreadPool(DataPartnerConstants.THREAD_COUNT);
        try {
            monitor.start();
            urlLogFileReader.start();
            List<Future<String>> futureList = new ArrayList<>();
            for (DataPartnerUpdateServiceImpl updateService : dataPartnerUpdateServices) {
                futureList.add(service.submit(updateService));
            }
            urlLogFileReader.join();

            for (int count = 0; count < DataPartnerConstants.THREAD_COUNT; count++) {
                sharedQueue.put(new ArrayList<>());
            }

            for (Future<String> future : futureList) {
                try {
                    LOGGER.info(future.get());
                } catch (ExecutionException e) {
                    LOGGER.error(e);
                }
            }
            synchronized (monitor) {
                monitor.notify();
            }
        } catch (InterruptedException e) {
            LOGGER.error(e);
        } finally {
            service.shutdown();
        }
        LOGGER.info("Completed generating data for " + logDate.toString());
    }

    public static void main(String[] args) {
        new DataPartnerRequestFinder(new Date()).process();
    }
}













