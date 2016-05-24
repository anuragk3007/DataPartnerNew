package model;

import java.io.Serializable;
import java.util.*;

/**
 * Created by kanchan.chowdhary
 * Date: 3/10/2016.
 */
public class DataPartnerDataVO implements Comparable<DataPartnerDataVO>, Serializable {
    private final String logDate;
    private final String dataPartnerName;
    private final String requestId;
    private Set<DataPartnerBehaviorVO> matchedBehaviorIdList;
    private boolean isFound;

    public DataPartnerDataVO(String dataPartnerName, String requestId) {
        this.logDate = "10-01-2016";
        this.dataPartnerName = dataPartnerName;
        this.requestId = requestId;
        matchedBehaviorIdList = new HashSet<>();
        isFound = false;
    }

    public DataPartnerDataVO(String logDate, String dataPartnerName, String requestId) {
        this.logDate = logDate;
        this.dataPartnerName = dataPartnerName;
        this.requestId = requestId;
        matchedBehaviorIdList = new HashSet<>();
        isFound = false;
    }

    public DataPartnerDataVO(String logDate, String dataPartnerName, String requestId, Set<DataPartnerBehaviorVO> matchedBehaviorIdList) {
        this.logDate = logDate;
        this.dataPartnerName = dataPartnerName;
        this.requestId = requestId;
        this.matchedBehaviorIdList = matchedBehaviorIdList;
        isFound = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataPartnerDataVO)) return false;

        DataPartnerDataVO dataVO = (DataPartnerDataVO) o;

        return logDate.equals(dataVO.logDate) && requestId.equals(dataVO.requestId);

    }

    @Override
    public int hashCode() {
        int result = logDate.hashCode();
        result = 31 * result + requestId.hashCode();
        return result;
    }

    @Override
    public int compareTo(DataPartnerDataVO o) {
        int result = o.logDate.compareTo(this.logDate);
        if (result == 0) {
            result = this.requestId.compareTo(o.requestId);
        }
        return result;
    }

    public String getLogDate() {
        return logDate;
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

    public void addBehavior(int behaviorId) {
        if (!isFound()) {
            setFound(true);
        }
        matchedBehaviorIdList.add(new DataPartnerBehaviorVO(behaviorId));
    }

    public void addBehavior(DataPartnerBehaviorVO behaviorVO) {
        if (!isFound()) {
            setFound(true);
        }
        matchedBehaviorIdList.add(behaviorVO);
    }

    public void addBehavior(int behaviorId, String topicPath, int ucUS, int ucUK) {
        if (!isFound()) {
            setFound(true);
        }
        matchedBehaviorIdList.add(new DataPartnerBehaviorVO(behaviorId, topicPath, ucUS, ucUK));
    }

    public String getRemarks() {
        String status = "Not Found";
        if (isFound) {
            if (matchedBehaviorIdList.isEmpty()) {
                status = "Not Matched";
            } else {
                status = "Matched";
            }
        }
        return status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!matchedBehaviorIdList.isEmpty()) {
            for (DataPartnerBehaviorVO behaviorVO : matchedBehaviorIdList) {
                sb.append(logDate).append(',').append(dataPartnerName).append(',').append(requestId)
                        .append(',').append(getRemarks()).append(',').append(matchedBehaviorIdList.size())
                        .append(',').append(behaviorVO.toString()).append('\n');
            }
        } else {
            sb.append(logDate).append(',').append(dataPartnerName).append(',').append(requestId)
                    .append(',').append(getRemarks()).append('\n');
        }
        return sb.toString();
    }
}
