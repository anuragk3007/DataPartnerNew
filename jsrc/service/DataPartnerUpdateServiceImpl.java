package service;

import elasticsearch.ElasticSearchClient;
import elasticsearch.ElasticSearchClientFactory;
import elasticsearch.ElasticSearchClientType;
import elasticsearch.ElasticSearchDocument;
import filereader.UrlBehaviorListGenerator;
import model.DataPartnerDataVO;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.EOFException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static utils.DataPartnerConstants.DATA_PARTNER_DATA_TTL;

/**
 * Created by kanchan.chowdhary on
 * 5/22/2016.
 */
public class DataPartnerUpdateServiceImpl implements DataPartnerUpdateService, Callable<String> {

    private static final Logger LOGGER = Logger.getLogger(DataPartnerUpdateServiceImpl.class);

    private final ElasticSearchClient client_;
    private final ObjectMapper objectMapper_;
    private final String logDate;
    private final BlockingQueue sharedQueue;
    private final CountDownLatch latch;
    private final UrlBehaviorListGenerator behaviorListGenerator;

    public DataPartnerUpdateServiceImpl(String logDate, BlockingQueue blockingQueue,
                                        CountDownLatch latch, UrlBehaviorListGenerator behaviorListGenerator) {
        client_ = ElasticSearchClientFactory.getClient(ElasticSearchClientType.REST_CLIENT);
        client_.setRetentionTimeInMs(DATA_PARTNER_DATA_TTL);
        objectMapper_ = new ObjectMapper();
        this.logDate = logDate;
        sharedQueue = blockingQueue;
        this.latch = latch;
        this.behaviorListGenerator = behaviorListGenerator;
    }

    public void addDataPartner(DataPartnerDataVO dataVO) {
        try {
            String jsonMsg = objectMapper_.writeValueAsString(dataVO);
            ElasticSearchDocument document = new ElasticSearchDocument(dataVO.getDataPartnerName(), dataVO.getLogDate()
                                                                        , dataVO.getRequestId(), jsonMsg);
            client_.index(document);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    @SuppressWarnings({"unchecked", "finally", "ReturnInsideFinallyBlock", "InfiniteLoopStatement"})
    public String call() {
        System.out.println("Started Update service: "+Thread.currentThread().getName());
        List<String> dataList;
        try {
            while (true) {
                try {
//                    System.out.println("Before consume "+Thread.currentThread().getName());
                    dataList = (List<String>) sharedQueue.take();
//                    System.out.println("After consume "+Thread.currentThread().getName());
//                    System.out.println("consumed by "+Thread.currentThread().getName()+" Data: "+dataList.toString());
                    long urlHash = Math.abs(Long.parseLong(dataList.get(0)));
                    String dataPartner = dataList.get(1);
                    String requestId = dataList.get(2);
                    if (!dataPartner.equalsIgnoreCase("ihg")) {
                        DataPartnerDataVO dataVO = new DataPartnerDataVO(logDate, dataPartner, requestId,
                                behaviorListGenerator.getBehaviorList(urlHash));
                                addDataPartner(dataVO);
                        System.out.println("Data: "+dataVO.toString());
                    } else {
                        Pattern ihgRequestPattern = Pattern.compile("(u[\\d]:[a-zA-Z ]+)");
                        Matcher matcher = ihgRequestPattern.matcher(requestId);
                        while (matcher.find()) {
                            DataPartnerDataVO dataVO = new DataPartnerDataVO(logDate, dataPartner, matcher.group(),
                                    behaviorListGenerator.getBehaviorList(urlHash));
                                    addDataPartner(dataVO);
                            System.out.println("Data: "+dataVO.toString());
                        }
                    }
                } catch (InterruptedException e) {
                    LOGGER.error(e);
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new EOFException();
                }
            }
        } finally {
            latch.countDown();
            return "Completed updating for thread : " + Thread.currentThread().getName();
        }
    }
}








