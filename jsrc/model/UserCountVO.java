package model;

import utils.DataPartnerConstants;

/**
 * Created by kanchan.chowdhary on
 * 5/17/2016.
 */
public class UserCountVO {
    private int usCount;
    private int ukCount;

    public UserCountVO(int usCount, int ukCount) {
        this.usCount = usCount;
        this.ukCount = ukCount;
    }

    public UserCountVO() {
        usCount = 0;
        ukCount = 0;
    }

    public void setCount(int geoId, int count) {
        if (geoId == DataPartnerConstants.US_GEO_ID) {
            usCount = count;
        } else if (geoId == DataPartnerConstants.UK_GEO_ID) {
            ukCount = count;
        }
    }

    public int getUsCount() {
        return usCount;
    }

    public int getUkCount() {
        return ukCount;
    }

    public void setUsCount(int usCount) {
        this.usCount = usCount;
    }

    public void setUkCount(int ukCount) {
        this.ukCount = ukCount;
    }

    public String toString() {
        return String.valueOf(usCount) + "," + String.valueOf(ukCount);
    }
}
