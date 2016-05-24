package utils;

import elasticsearch.*;
import filereader.TopicPathGenerator;
import model.DataPartnerDataVO;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import static utils.DataPartnerConstants.DATA_PARTNER_DATA_TTL;
import static utils.DataPartnerConstants.DATA_PARTNER_REPORT_DATE_FORMAT;

/**
 * Created by anurag.krishna
 * Date: 3/11/2016.
 */
public class DataPartnerDataReader {

    private ElasticSearchClient client_;
    private ObjectMapper objectMapper_;
    private final TopicPathGenerator topicGenerator;

    public DataPartnerDataReader() {
        client_ = ElasticSearchClientFactory.getClient(ElasticSearchClientType.REST_CLIENT);
        client_.setRetentionTimeInMs(DATA_PARTNER_DATA_TTL);
        objectMapper_ = new ObjectMapper();
        topicGenerator = new TopicPathGenerator();
    }

    private void readData() throws IOException {
        System.out.println("Started reading urlLog");
        File dataPartnerFile = new File("Data/datapartner.txt");
        if (dataPartnerFile.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(dataPartnerFile));
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                count++;
                if (count % 100 == 0) {
                    System.out.println("Completed: " + count);
                    break;
                }
                String[] values = line.split("\t");
                String dataPartner = values[0];
                String requestId = values[1];
                DataPartnerDataVO dataVO = new DataPartnerDataVO(dataPartner, requestId);
                try {
                    String[] behaviorsString = values[3].split(",");
                    HashSet<String> behaviorIds = new HashSet<>(Arrays.asList(behaviorsString));
                    for (String behString : behaviorIds) {
                        dataVO.addBehavior(topicGenerator.getBehavior(Integer.parseInt(behString)));
                    }
                }catch (ArrayIndexOutOfBoundsException e){}
                String jsonMsg = objectMapper_.writeValueAsString(dataVO);
                String indexDate = DATA_PARTNER_REPORT_DATE_FORMAT.format(new Date());
//                ElasticSearchDocument document = new ElasticSearchDocument(dataPartner, indexDate, requestId, jsonMsg);
                ElasticSearchDocument document = new ElasticSearchDocument(dataPartner, indexDate, requestId, jsonMsg);
                client_.index(document);
                indexDate = DATA_PARTNER_REPORT_DATE_FORMAT.format(new Date(System.currentTimeMillis() - DataPartnerConstants.MILLI_SECONDS_COUNT));
                document = new ElasticSearchDocument(dataPartner, indexDate, requestId, jsonMsg);
                client_.index(document);
                indexDate = DATA_PARTNER_REPORT_DATE_FORMAT.format(new Date(System.currentTimeMillis() - 2 * DataPartnerConstants.MILLI_SECONDS_COUNT));
                document = new ElasticSearchDocument(dataPartner, indexDate, requestId, jsonMsg);
                client_.index(document);
                indexDate = DATA_PARTNER_REPORT_DATE_FORMAT.format(new Date(System.currentTimeMillis() - 3 * DataPartnerConstants.MILLI_SECONDS_COUNT));
                document = new ElasticSearchDocument(dataPartner, indexDate, requestId, jsonMsg);
                client_.index(document);
            }
        }
    }

    public static void main(String[] args) throws IOException {
//        ReportsDataVO.getInstance().generateDates();
        new DataPartnerDataReader().readData();
    }
}
