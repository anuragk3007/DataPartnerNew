package utils;

import java.text.SimpleDateFormat;

/**
 * Created by anurag.krishna
 * Date: 3/11/2016.
 */
public class DataPartnerConstants {

    public static final String DATA_PARTNER_INDEX = "datapartnerreqindex";
    public static final SimpleDateFormat DATA_PARTNER_REPORT_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    public static final int DATA_PARTNER_DATA_TTL = 7 * 24 * 60 * 60 * 1000;
}
