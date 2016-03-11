package model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by kanchan.chowdhary
 * Date: 3/10/2016.
 */
public class DataPartnerDataVO {
    private final String dataPartnerName;
    private final String requestId;
    private Set<String> matchedBehaviorIdList;

    public DataPartnerDataVO(String dataPartnerName, String requestId) {
        this.dataPartnerName = dataPartnerName;
        this.requestId = requestId;
        matchedBehaviorIdList = new HashSet<>();
    }

    public DataPartnerDataVO(String dataPartnerName, String requestId, Set<String> matchedBehaviorIdList) {
        this.dataPartnerName = dataPartnerName;
        this.requestId = requestId;
        this.matchedBehaviorIdList = matchedBehaviorIdList;
    }

    public String getDataPartnerName() {
        return dataPartnerName;
    }

    public String getRequestId() {
        return requestId;
    }

    public Set getBehaviorList() {
        return matchedBehaviorIdList;
    }

    public void addBehavior(String behaviorId) {
        matchedBehaviorIdList.add(behaviorId);
    }

    private String getStatus() {
        if (matchedBehaviorIdList.isEmpty()) {
            return "Request Found but not Matched";
        } else {
            return "Request Found and Matched\t" + matchedBehaviorIdList.toString().replaceAll("[\\[\\]]*", "").replaceAll(", ", ",");
        }
    }

    @Override
    public String toString() {
        return dataPartnerName + "\t" + requestId + "\t" + getStatus();
    }
}
