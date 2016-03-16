package utils;

import elasticsearch.ElasticSearchClient;
import elasticsearch.ElasticSearchClientFactory;
import elasticsearch.ElasticSearchClientType;
import elasticsearch.ElasticSearchDocument;
import model.DataPartnerDataVO;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static utils.DataPartnerConstants.DATA_PARTNER_DATA_TTL;
import static utils.DataPartnerConstants.DATA_PARTNER_REPORT_DATE_FORMAT;

/**
 * Created by anurag.krishna
 * Date: 3/11/2016.
 */
public class DataPartnerDataReader {

    private ElasticSearchClient client_;
    private ObjectMapper objectMapper_;

    public DataPartnerDataReader() {
        client_ = ElasticSearchClientFactory.getClient(ElasticSearchClientType.REST_CLIENT);
        client_.setRetentionTimeInMs(DATA_PARTNER_DATA_TTL);
        objectMapper_ = new ObjectMapper();
    }

    private void readData() throws IOException {
        File dataPartnerFile = new File("Data/datapartner.txt");
        if (dataPartnerFile.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(dataPartnerFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\\t");
                String dataPartner = values[0];
                String requestId = values[1];
                String[] behaviorsString = values[values.length - 1].split(",");
                Set<String> behaviorIds = new HashSet<>();
                behaviorIds.addAll(Arrays.asList(behaviorsString));
                DataPartnerDataVO dataVO = new DataPartnerDataVO(dataPartner, requestId, behaviorIds);

                String jsonMsg = objectMapper_.writeValueAsString(dataVO);
                String elasticSearchType = DATA_PARTNER_REPORT_DATE_FORMAT.format(new Date());
                ElasticSearchDocument document = new ElasticSearchDocument(dataPartner, elasticSearchType, requestId, jsonMsg);
                client_.index(document);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new DataPartnerDataReader().readData();
    }
}
