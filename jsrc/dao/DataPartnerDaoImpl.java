package dao;

import elasticsearch.ElasticSearchTransportClient;
import model.DataPartnerDataVO;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.*;

/**
 * Created by anurag.krishna 3/16/2016.
 */
public class DataPartnerDaoImpl implements DataPartnerDao {

    public DataPartnerDaoImpl() {

    }

    @Override
    public List<DataPartnerDataVO> getDataPartnerForRequestId(String dataPartner, String requestId) {
        Client elasticSearchClient = new ElasticSearchTransportClient().getElasticSearchTransportClient();
        List<DataPartnerDataVO> dataPartnerDataVOs = new ArrayList<>();
        SearchResponse response = elasticSearchClient.prepareSearch(dataPartner).setQuery(QueryBuilders.matchQuery("requestId", requestId)).execute()
                .actionGet();

        SearchHits searchHits = response.getHits();
        System.out.println(searchHits.getTotalHits());
        for (SearchHit searchHit : searchHits.getHits()) {
            Map<String, Object> sources = searchHit.getSource();
            ArrayList<String> behs = (ArrayList) sources.get("behaviorList");
            Set<String> behaviorIds = new HashSet<>();
            behaviorIds.addAll(behs);
            DataPartnerDataVO dataVO = new DataPartnerDataVO((String) sources.get("dataPartnerName"),
                    (String) sources.get("requestId"), behaviorIds);
            dataPartnerDataVOs.add(dataVO);
            System.out.println(searchHit.getSourceAsString());
        }

        elasticSearchClient.close();
        return dataPartnerDataVOs;
    }

    @Override
    public DataPartnerDataVO getDataPartnerForRequestIdForDate(String dataPartner, String requestId, String date) {
        DataPartnerDataVO dataVO = null;
        Client elasticSearchClient = new ElasticSearchTransportClient().getElasticSearchTransportClient();
        SearchResponse response = elasticSearchClient.prepareSearch(dataPartner).setTypes(date)
                .setQuery(QueryBuilders.matchQuery("requestId", requestId)).execute()
                .actionGet();

        SearchHits searchHits = response.getHits();
        System.out.println(searchHits.getTotalHits());
        for (SearchHit searchHit : searchHits.getHits()) {
            Map<String, Object> sources = searchHit.getSource();
            ArrayList<String> behs = (ArrayList) sources.get("behaviorList");
            Set<String> behaviorIds = new HashSet<>();
            behaviorIds.addAll(behs);
            dataVO = new DataPartnerDataVO((String) sources.get("dataPartnerName"),
                    (String) sources.get("requestId"), behaviorIds);

            System.out.println(searchHit.getSourceAsString());
        }

        elasticSearchClient.close();
        return dataVO;
    }

    @Override
    public List<DataPartnerDataVO> getAllDataPartnerRequestIds(String dataPartner) {
        Client elasticSearchClient = new ElasticSearchTransportClient().getElasticSearchTransportClient();
        List<DataPartnerDataVO> dataPartnerDataVOs = new ArrayList<>();
        SearchResponse response = elasticSearchClient.prepareSearch(dataPartner).execute().actionGet();

        SearchHits searchHits = response.getHits();
        System.out.println(searchHits.getTotalHits());
        for (SearchHit searchHit : searchHits.getHits()) {
            Map<String, Object> sources = searchHit.getSource();
            ArrayList<String> behs = (ArrayList) sources.get("behaviorList");
            Set<String> behaviorIds = new HashSet<>();
            behaviorIds.addAll(behs);
            DataPartnerDataVO dataVO = new DataPartnerDataVO((String) sources.get("dataPartnerName"),
                    (String) sources.get("requestId"), behaviorIds);
            dataPartnerDataVOs.add(dataVO);
            System.out.println(searchHit.getSourceAsString());
        }

        elasticSearchClient.close();
        return dataPartnerDataVOs;
    }

    @Override
    public List<DataPartnerDataVO> getAllDataPartnerRequestIdsForDate(String dataPartner, String date) {
        Client elasticSearchClient = new ElasticSearchTransportClient().getElasticSearchTransportClient();
        List<DataPartnerDataVO> dataPartnerDataVOs = new ArrayList<>();
        SearchResponse response = elasticSearchClient.prepareSearch(dataPartner).setTypes(date).execute()
                .actionGet();

        SearchHits searchHits = response.getHits();
        System.out.println(searchHits.getTotalHits());
        for (SearchHit searchHit : searchHits.getHits()) {
            Map<String, Object> sources = searchHit.getSource();
            ArrayList<String> behs = (ArrayList) sources.get("behaviorList");
            Set<String> behaviorIds = new HashSet<>();
            behaviorIds.addAll(behs);
            DataPartnerDataVO dataVO = new DataPartnerDataVO((String) sources.get("dataPartnerName"),
                    (String) sources.get("requestId"), behaviorIds);
            dataPartnerDataVOs.add(dataVO);
            System.out.println(searchHit.getSourceAsString());
        }

        elasticSearchClient.close();
        return dataPartnerDataVOs;
    }

    public static void main(String[] args) {
        new DataPartnerDaoImpl().getDataPartnerForRequestIdForDate("adobe", "1721133", "16-03-2016");
    }
}
