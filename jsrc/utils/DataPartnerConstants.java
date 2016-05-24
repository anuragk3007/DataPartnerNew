package utils;

import java.text.SimpleDateFormat;

/**
 * Created by anurag.krishna
 * Date: 3/11/2016.
 */
public class DataPartnerConstants {

    public static final SimpleDateFormat DATA_PARTNER_REPORT_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    public static final SimpleDateFormat DATA_PARTNER_UI_DATE_FORMAT = new SimpleDateFormat("MMM-dd-yyyy");
    public static final int MILLI_SECONDS_COUNT = 24 * 60 * 60 * 1000;
    public static final int DATA_PARTNER_DATA_TTL = 7 * MILLI_SECONDS_COUNT;
    public static final int US_GEO_ID = 3585;
    public static final int UK_GEO_ID = 3569;
    public static final int THREAD_COUNT = 5;
    public static final int DATA_MAX_BATCH_SIZE = 20;
    public static final int MONITOR_INTERVAL = 10 * 60 * 1000;
}
