package dao;

import com.carrotsearch.hppc.ObjectLookupContainer;
import com.carrotsearch.hppc.cursors.ObjectCursor;
import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import elasticsearch.ElasticSearchTransportClient;
import model.DataPartnerBehaviorVO;
import model.DataPartnerDataVO;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import utils.DataPartnerConstants;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by anurag.krishna 3/16/2016.
 */
public class DataPartnerDaoImpl implements DataPartnerDao {

    public DataPartnerDaoImpl() {

    }

    @Override
    public List<String> getAllDataPartnersName() {
        List<String> dataPartnerNameList = new ArrayList<>();
        Client elasticSearchClient = new ElasticSearchTransportClient().getElasticSearchTransportClient();
        ObjectLookupContainer<String> indicesList = elasticSearchClient.admin().cluster().prepareState().execute().
                actionGet().getState().getMetaData().indices().keys();
        for (ObjectCursor dataPartner : indicesList) {
            dataPartnerNameList.add(dataPartner.value.toString());
        }
        return dataPartnerNameList;
    }

    @Override
    public List<String> getDateListForDataPartner(String dataPartner) {
        List<String> dateList = new ArrayList<>();
        try (Client elasticSearchClient = new ElasticSearchTransportClient().getElasticSearchTransportClient()){
            GetMappingsResponse res = elasticSearchClient.admin().indices().getMappings(new GetMappingsRequest().indices(dataPartner)).get();
            ImmutableOpenMap<String, MappingMetaData> mapping = res.mappings().get(dataPartner);
            for (ObjectObjectCursor<String, MappingMetaData> c : mapping) {
                dateList.add(c.key);
            }
        } catch (InterruptedException | ExecutionException ignored) {
        }
        return dateList;
    }

    @Override
    public DataPartnerDataVO getAllDataPartnerDataForDataPartner(String dataPartner) {
        DataPartnerDataVO dataVO = null;
        try (Client elasticSearchClient = new ElasticSearchTransportClient().getElasticSearchTransportClient()) {
            SearchResponse response = elasticSearchClient.prepareSearch(dataPartner).execute().actionGet();

            SearchHits searchHits = response.getHits();
            SearchHit searchHit = searchHits.getHits()[0];
            dataVO = getDataPartnerFromSearchHit(searchHit);
        } catch (IndexNotFoundException ignored) {
        }
        return dataVO;
    }

    @Override
    public List<DataPartnerDataVO> getAllDataPartnerRequestIdsForDate(String date, String dataPartner) {
        List<DataPartnerDataVO> dataPartnerDataVOs = new ArrayList<>();
        try (Client elasticSearchClient = new ElasticSearchTransportClient().getElasticSearchTransportClient()) {
            SearchResponse response = elasticSearchClient.prepareSearch(dataPartner).setTypes(date)
                    .setSize(DataPartnerConstants.DATA_MAX_BATCH_SIZE).execute()
                    .actionGet();

            SearchHits searchHits = response.getHits();
            for (SearchHit searchHit : searchHits.getHits()) {
                DataPartnerDataVO dataVO = getDataPartnerFromSearchHit(searchHit);
                dataPartnerDataVOs.add(dataVO);
            }

        } catch (IndexNotFoundException ignored) {

        }
        return dataPartnerDataVOs;
    }

    @Override
    public DataPartnerDataVO getDataPartnerForRequestIdForDate(String date, String dataPartner, String requestId) {
        DataPartnerDataVO dataVO = new DataPartnerDataVO(date, dataPartner, requestId);
        try (Client elasticSearchClient = new ElasticSearchTransportClient().getElasticSearchTransportClient()) {
            SearchResponse response = elasticSearchClient.prepareSearch(dataPartner).setTypes(date)
                    .setQuery(QueryBuilders.matchQuery("requestId", requestId)).execute()
                    .actionGet();

            SearchHits searchHits = response.getHits();
            for (SearchHit searchHit : searchHits.getHits()) {
                dataVO = getDataPartnerFromSearchHit(searchHit);
            }
        } catch (IndexNotFoundException ignored) {

        }
        return dataVO;
    }

    private DataPartnerDataVO getDataPartnerFromSearchHit(SearchHit searchHit) {
        Map<String, Object> sources = searchHit.getSource();
        ArrayList behs = (ArrayList) sources.get("behaviorList");
        Set<DataPartnerBehaviorVO> behaviorIds = new HashSet<>();
        for (Object behaviorObject : behs) {
            Map behavior = (Map) behaviorObject;
            int behId = (Integer) behavior.get("behaviorId");
            String topicPath = (String) behavior.get("topicPath");
            Map userCount = (Map) behavior.get("userCount");
            int ucUS = (Integer) userCount.get("usCount");
            int ucUK = (Integer) userCount.get("ukCount");
            behaviorIds.add(new DataPartnerBehaviorVO(behId, topicPath, ucUS, ucUK));
        }
        return new DataPartnerDataVO((String) sources.get("logDate"), (String) sources.get("dataPartnerName"),
                (String) sources.get("requestId"), behaviorIds);
    }


    public static void main(String[] args) {
        new DataPartnerDaoImpl().getDataPartnerForRequestIdForDate("adobe", "1721133", "16-03-2016");
    }
}
