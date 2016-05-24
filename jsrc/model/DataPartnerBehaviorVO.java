package model;

import java.io.Serializable;

/**
 * Created by kanchan.chowdhary on
 * 5/4/2016.
 */
public class DataPartnerBehaviorVO implements Serializable {
    private int behaviorId;
    private String topicPath;
    private UserCountVO userCount;

    public DataPartnerBehaviorVO(int behaviorId) {
        this.behaviorId = behaviorId;
        topicPath = "";
        userCount = new UserCountVO();
    }

    public DataPartnerBehaviorVO(int behaviorId, String topicPath, int userCountUS, int userCountUK) {
        this.behaviorId = behaviorId;
        this.topicPath = topicPath;
        userCount = new UserCountVO(userCountUS, userCountUK);
    }

    public DataPartnerBehaviorVO(int behaviorId, String topicPath, UserCountVO userCount) {
        this.behaviorId = behaviorId;
        this.topicPath = topicPath;
        this.userCount = userCount;
    }

    public int getBehaviorId() {
        return behaviorId;
    }

    public String getTopicPath() {
        return topicPath;
    }

    public String getTopicName() {
        String[] list = topicPath.split(" -> ");
        return list[list.length-1];
    }

    public void setTopicPath(String topicPath) {
        this.topicPath = topicPath;
    }

    public void setBehaviorId(int behaviorId) {
        this.behaviorId = behaviorId;
    }

    public UserCountVO getUserCount() {
        return userCount;
    }

    public void setUserCount(UserCountVO userCount) {
        this.userCount = userCount;
    }

    @Override
    public String toString() {
        return String.valueOf(behaviorId) + ",\"" + topicPath + "\"," + userCount.toString();
    }
}
