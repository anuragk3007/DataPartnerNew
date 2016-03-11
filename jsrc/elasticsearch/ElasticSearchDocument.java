package elasticsearch;

/**
 * User: prashant.saksena
 * Date: 9/12/14
 */
public class ElasticSearchDocument {
    private String index_;
    private String type_;
    private String id_;
    private String data_;

    public ElasticSearchDocument(String index, String type, String id, String data) {
        index_ = index;
        type_ = type;
        id_ = id;
        data_ = data;
    }

    public String getIndex() {
        return index_;
    }

    public String getType() {
        return type_;
    }

    public String getId() {
        return id_;
    }

    public String getData() {
        return data_;
    }
}
