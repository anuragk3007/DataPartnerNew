package elasticsearch;

import org.apache.log4j.Logger;

import java.util.Calendar;

/**
 * User: prashant.saksena
 * Date: 9/12/14
 */
public abstract class ElasticSearchClient {
    private static final Logger LOGGER = Logger.getLogger(ElasticSearchClient.class);

    protected String host_;
    protected Integer port_;
    protected Integer retentionInMs_;

    protected final String DEFAULT_VERSION = "1";
    protected final String VERSION_TYPE = "force";

    public ElasticSearchClient() {
        host_ = "localhost";
        port_ = 9200;
    }

    public abstract boolean index(ElasticSearchDocument document);

    public abstract ElasticSearchClientType getType();

    protected long getTTL() {
        Calendar calendar = Calendar.getInstance();
        long currentTime = calendar.getTime().getTime();

        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.add(Calendar.MILLISECOND, retentionInMs_);
        long retentionTime = calendar.getTime().getTime();

        long ttl = retentionTime - currentTime;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("TTL : " + ttl);
        }

        return ttl;
    }

    public void setRetentionTimeInMs(int retentionInMs) {
        retentionInMs_ = retentionInMs;
    }
}
