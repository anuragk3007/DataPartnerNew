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
        isFound = true;
        isMatched = false;
    }

    public DataPartnerDataVO(String dataPartnerName, String requestId, boolean isFound) {
        this.dataPartnerName = dataPartnerName;
        this.requestId = requestId;
        matchedBehaviorIdList = new HashSet<>();
        this.isFound = isFound;
        isMatched = false;
    }

    public DataPartnerDataVO(String dataPartnerName, String requestId, Set<String> matchedBehaviorIdList) {
        this.dataPartnerName = dataPartnerName;
        this.requestId = requestId;
        this.matchedBehaviorIdList = matchedBehaviorIdList;
        isFound = true;
        isMatched = true;
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
        String status = "Request not found";
        if (isFound) {
            if (!isMatched) {
                status = "Request Found but not Matched";
            } else {
                status = "Request Found and Matched";/*\t" + matchedBehaviorIdList.toString().replaceAll("[\\[\\]]*", "").replaceAll(", ", ";");*/
            }
        }
        return status;
    }

    @Override
    public String toString() {
        return '\"'+requestId+"\","+getStatus()+','+matchedBehaviorIdList.toString().replaceAll("[\\[\\]]*", "").replaceAll(", ", ";")+'\n';
//        return dataPartnerName + "\t" + requestId + "\t" + getStatus();
    }
}
