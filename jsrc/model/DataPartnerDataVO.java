package model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by kanchan.chowdhary
 * Date: 3/10/2016.
 */
public class DataPartnerDataVO implements Comparable<DataPartnerDataVO>{
    private final String dataPartnerName;
    private final String requestId;
    private Set<String> matchedBehaviorIdList;
    private boolean isFound;
    private boolean isMatched;

    public DataPartnerDataVO(String dataPartnerName, String requestId) {
        this.dataPartnerName = dataPartnerName;
        this.requestId = requestId;
        matchedBehaviorIdList = new HashSet<>();
        isFound = false;
        isMatched = false;
    }

    public DataPartnerDataVO(String dataPartnerName, String requestId, Set<String> matchedBehaviorIdList) {
        this.dataPartnerName = dataPartnerName;
        this.requestId = requestId;
        this.matchedBehaviorIdList = matchedBehaviorIdList;
        isFound = false;
        isMatched = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataPartnerDataVO)) return false;

        DataPartnerDataVO that = (DataPartnerDataVO) o;

        return requestId.equals(that.requestId);

    }

    @Override
    public int hashCode() {
        return requestId.hashCode();
    }

    @Override
    public int compareTo(DataPartnerDataVO o) {
        return requestId.compareTo(o.requestId);
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

    public boolean isFound() {
        return isFound;
    }

    public void setFound(boolean found) {
        isFound = found;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void addBehavior(String behaviorId) {
        isMatched = true;
        matchedBehaviorIdList.add(behaviorId);
    }

    public String getStatus() {
        if (matchedBehaviorIdList.isEmpty()) {
            return "Request Found but not Matched";
        } else {
            return "Request Found and Matched\t" + matchedBehaviorIdList.toString().replaceAll("[\\[\\]]*", "").replaceAll(", ", ";");
        }
    }

    @Override
    public String toString() {
        return dataPartnerName + "\t" + requestId + "\t" + getStatus();
    }
}
