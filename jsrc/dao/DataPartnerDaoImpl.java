package dao;

import elasticsearch.ElasticSearchTransportClient;
import model.DataPartnerDataVO;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anurag.krishna on 3/16/2016.
 */
public class DataPartnerDaoImpl implements DataPartnerDao {
    private Client elasticSearchClient_;

    public DataPartnerDaoImpl() {
        elasticSearchClient_ = new ElasticSearchTransportClient().getElasticSearchTransportClient();
    }

    @Override
    public List<DataPartnerDataVO> getDataPartnerForRequestId(String dataPartner, String requestId) {
        List<DataPartnerDataVO> dataPartnerDataVOs = new ArrayList<>();
        SearchResponse response = elasticSearchClient_.prepareSearch(dataPartner).execute()
                .actionGet();

        SearchHits searchHits = response.getHits();
        System.out.println(searchHits.getTotalHits());
        for (SearchHit searchHit : searchHits) {
            System.out.println(searchHit.getSourceAsString());
        }

        elasticSearchClient_.close();
        return dataPartnerDataVOs;
    }

    @Override
    public DataPartnerDataVO getDataPartnerForRequestIdForDate(String dataPartner, String requestId, String date) {
        return null;
    }

    @Override
    public List<DataPartnerDataVO> getAllDataPartnerRequestIds(String dataPartner) {
        return null;
    }

    @Override
    public List<DataPartnerDataVO> getAllDataPartnerRequestIdsForDate(String dataPartner, String date) {
        return null;
    }
}
