package elasticsearch;

import org.apache.log4j.Logger;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;

/**
 * User: prashant.saksena
 * Date: 9/12/14
 */
public class ElasticSearchRestClient extends ElasticSearchClient {
    private static final Logger LOGGER = Logger.getLogger(ElasticSearchRestClient.class);

    private String baseUrl_;
    private Client restClient_;

    public ElasticSearchRestClient() {
        super();
        baseUrl_ = "http://" + host_ + ":" + port_;
        restClient_ = ClientBuilder.newClient();
    }

    public boolean index(ElasticSearchDocument document) {
        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl_).append("/").append(document.getIndex()).append("/").append(document.getType()).append("/").append(document.getId())
                .append("?").append("version=").append(DEFAULT_VERSION).append("&").append("version_type=").append(VERSION_TYPE);
        if (retentionInMs_ > 0) {
            sb.append("&").append("ttl=").append(String.valueOf(getTTL())).append("ms");
        }

        String endPoint = sb.toString();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("End Point: " + endPoint);
        }

        WebTarget webTarget = restClient_.target(endPoint);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        invocationBuilder.accept(MediaType.APPLICATION_JSON_TYPE);
        Response postResponse = invocationBuilder.post(Entity.entity(document.getData(), MediaType.APPLICATION_JSON));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Status = " + postResponse.getStatus());
            LOGGER.debug("Response = " + postResponse.readEntity(String.class));
        }

        boolean isSaved = postResponse.getStatus() == HttpURLConnection.HTTP_ACCEPTED
                || postResponse.getStatus() == HttpURLConnection.HTTP_OK || postResponse.getStatus() == HttpURLConnection.HTTP_CREATED;
        if (!isSaved) {
            LOGGER.info("Elastic Search Response Status : " + postResponse.getStatus() + ", Response: " + postResponse.readEntity(String.class));
        }
        return isSaved;
    }

    @Override
    public ElasticSearchClientType getType() {
        return ElasticSearchClientType.REST_CLIENT;
    }

}
