package model;

import org.apache.log4j.Logger;
import service.DataPartnerSearchService;
import service.DataPartnerSearchServiceImpl;
import utils.DataPartnerConstants;

import java.text.ParseException;
import java.util.*;

/**
 * Created by kanchan.chowdhary on
 * 5/18/2016.
 */
public class ReportsDataVO {
    private static final Logger LOGGER = Logger.getLogger(ReportsDataVO.class);
    private static volatile ReportsDataVO INSTANCE = null;

    public static ReportsDataVO getInstance() {
        if (INSTANCE == null) {
            synchronized (ReportsDataVO.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ReportsDataVO();
                }
            }
        }
        return INSTANCE;
    }

    private final List<String> dataPartnerNameList;
    private final List<String> reportDateList;
    private boolean append;

    private ReportsDataVO() {
        dataPartnerNameList = new ArrayList<>();
        reportDateList = new ArrayList<>();
        append = false;
        generateData(new DataPartnerSearchServiceImpl());
    }

    public List<String> getDataPartnerNameList() {
        return dataPartnerNameList;
    }

    public List<String> getReportDateList() {
        return reportDateList;
    }

    public boolean isAppend() {
        return append;
    }

    public void setAppend(boolean append) {
        this.append = append;
    }

    public void generateData(DataPartnerSearchService searchService) {
        dataPartnerNameList.clear();
        dataPartnerNameList.addAll(searchService.getAllDataPartnersName());
        Collections.sort(dataPartnerNameList);
        Set<Date> dateList = new TreeSet<>();
        for (String dataPartner : dataPartnerNameList) {
            List<String> dateStringList = searchService.getDateListForDataPartner(dataPartner);
            for (String dateString : dateStringList) {
                try {
                    dateList.add(DataPartnerConstants.DATA_PARTNER_REPORT_DATE_FORMAT.parse(dateString));
                } catch (ParseException e) {
                    LOGGER.error(e);
                }
            }
        }
        reportDateList.clear();
        for (Date date : dateList) {
            reportDateList.add(DataPartnerConstants.DATA_PARTNER_UI_DATE_FORMAT.format(date));
        }
    }

    public static void main(String[] args) {
        ReportsDataVO obj = ReportsDataVO.getInstance();
    }
}
