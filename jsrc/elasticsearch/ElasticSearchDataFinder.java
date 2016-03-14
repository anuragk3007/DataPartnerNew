package elasticsearch;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by anurag.krishna on 3/14/2016.
 */
public class ElasticSearchDataFinder {

    public void connectElastic() throws UnknownHostException {
        Client client = TransportClient.builder().build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        SearchResponse response = client.prepareSearch("datapartnerreqindex").execute()
                .actionGet();

        SearchHits searchHits = response.getHits();
        System.out.println(searchHits.getTotalHits());
        for (SearchHit searchHit : searchHits) {
            System.out.println(searchHit.getSourceAsString());
        }

        client.close();
    }

    public static void main(String[] args) throws UnknownHostException {
        new ElasticSearchDataFinder().connectElastic();
    }
}
