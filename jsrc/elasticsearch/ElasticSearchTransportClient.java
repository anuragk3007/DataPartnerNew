package elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by anurag.krishna
 * Date: 3/14/2016.
 */
public class ElasticSearchTransportClient extends ElasticSearchClient {

    public Client getElasticSearchTransportClient() {
        Client client = null;
        try {
            client = TransportClient.builder().build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        } catch (UnknownHostException ex) {

        }

        return client;
    }

    @Override
    public boolean index(ElasticSearchDocument document) {
        return false;
    }

    @Override
    public ElasticSearchClientType getType() {
        return ElasticSearchClientType.TRANSPORT_CLIENT;
    }

    public static void main(String[] args) throws UnknownHostException {
    }
}
