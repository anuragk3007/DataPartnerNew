package elasticsearch;

import exception.DPSystemException;
import org.apache.log4j.Logger;

/**
 * User: prashant.saksena
 * Date: 9/12/14
 */
public class ElasticSearchClientFactory {
    private static final Logger LOGGER = Logger.getLogger(ElasticSearchClientFactory.class);

    private volatile static ElasticSearchClient elasticSearchClient_;

    public static ElasticSearchClient getClient(ElasticSearchClientType elasticSearchClientType) {
        if (elasticSearchClient_ == null) {
            synchronized (ElasticSearchClientFactory.class) {
                if (elasticSearchClient_ == null) {
                    switch (elasticSearchClientType) {
                        case REST_CLIENT:
                            elasticSearchClient_ = new ElasticSearchRestClient();
                            break;
                        case JAVA_API_CLIENT:
                            throw new DPSystemException("Java client not defined");
                    }
                }
            }
        }

        // check that only one type instance exists
        if (!elasticSearchClient_.getType().equals(elasticSearchClientType)) {
            throw new DPSystemException("Trying to create a different elastic search client");
        }
        return elasticSearchClient_;
    }
}
