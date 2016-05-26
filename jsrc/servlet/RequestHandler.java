package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import properties.DPProperties;
import model.DataPartnerDataVO;
import model.ReportsDataVO;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import service.DataPartnerSearchService;
import service.DataPartnerSearchServiceImpl;
import utils.DataPartnerConstants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.util.*;

/**
 * Created by kanchan.chowdhary on
 * 3/15/2016.
 */
public class RequestHandler extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(RequestHandler.class);

    private DataPartnerSearchService searchService;
    private ReportsDataVO reportsDataVO;

    public RequestHandler() {
        searchService = new DataPartnerSearchServiceImpl();
        reportsDataVO = ReportsDataVO.getInstance();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, String[]> parameterMap = request.getParameterMap();
        Set<DataPartnerDataVO> searchQueryResult = new TreeSet<>();


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (parameterMap.get("requestType")[0].equals("reportDataQuery")) {
            LOGGER.info("Data request to load page arrived at: "+new Date());
            JsonObject responseObject = new JsonObject();
            responseObject.addProperty("success", true);
            reportsDataVO.setAppend(false);
            JSONArray jsonDateList = new JSONArray();
            for (String date : reportsDataVO.getReportDateList()) {
                jsonDateList.put(date);
            }
            JSONArray jsonDataPartnerList = new JSONArray();
            for (String dataPartnerName : reportsDataVO.getDataPartnerNameList()) {
                jsonDataPartnerList.put(dataPartnerName);
            }
            Gson gson = new Gson();
            responseObject.add("dataPartnerNameList", gson.toJsonTree(jsonDataPartnerList));
            responseObject.add("dateList", gson.toJsonTree(jsonDateList));
            response.getWriter().print(responseObject);
        } else {
            LOGGER.info("Query request arrived at "+new Date());
            String dataPartner = parameterMap.get("dataPartner")[0];
            String sDateString = parameterMap.get("startDate")[0];
            String eDateString = parameterMap.get("endDate")[0];
            String[] requestIdList = parameterMap.get("requestIdList")[0].split("\n|\t");
            searchQueryResult.addAll(getQueryResult(sDateString, eDateString, dataPartner, requestIdList));
            Gson gson = new Gson();
            JSONArray jsonResultList = new JSONArray();
            ObjectMapper mapper = new ObjectMapper();
            StringBuilder resultCsvString = new StringBuilder();
            for (DataPartnerDataVO result : searchQueryResult) {
                resultCsvString.append(result.toString());
                String jsonObject = mapper.writeValueAsString(result);
                jsonResultList.put(jsonObject);
            }
            writeResult(resultCsvString);
            JsonElement jsonElement = gson.toJsonTree(jsonResultList);
            JsonObject responseObject = new JsonObject();
            responseObject.addProperty("success", true);
            responseObject.addProperty("resultFile", DPProperties.relativeResultFilePath());
            responseObject.add("resultList", jsonElement);

            response.getWriter().print(responseObject);
        }
    }

    private void writeResult(StringBuilder resultCSVString) {
        String resultFilePath = DPProperties.absoluteResultFilePath();
        File resultDirectory = new File(DPProperties.absoluteResultDirPath());
        if (!resultDirectory.exists()) {
            resultDirectory.mkdirs();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(resultFilePath, reportsDataVO.isAppend()))){
            if (!reportsDataVO.isAppend()) {
                writer.write("Date,Data Partner,Request Id,Remarks,# Behavior,Behavior Id,Topic Path,US User Count,UK User Count\n");
                reportsDataVO.setAppend(true);
            }
            writer.write(resultCSVString.toString());
            writer.flush();
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    public List<DataPartnerDataVO> getQueryResult(String sDateString, String eDateString,
                                                  String dataPartner, String[] requestIdList) {
        LOGGER.info("Querying for: " + sDateString + "\t" + eDateString + "\t" + dataPartner + "\t" + Arrays.asList(requestIdList).toString());
        List<DataPartnerDataVO> resultList = new ArrayList<>();
        try {
            for (int index = reportsDataVO.getReportDateList().indexOf(sDateString);
                 index <= reportsDataVO.getReportDateList().indexOf(eDateString); index++) {
                try {
                    String currentDate = DataPartnerConstants.DATA_PARTNER_REPORT_DATE_FORMAT.format(
                            DataPartnerConstants.DATA_PARTNER_UI_DATE_FORMAT.parse(
                                    reportsDataVO.getReportDateList().get(index)));
                    if (requestIdList[0].equalsIgnoreCase("All")) {
                        List<DataPartnerDataVO> dataVOList = searchService.getAllDataPartnerRequestForDate(currentDate, dataPartner);
                        resultList.addAll(dataVOList);
                    } else {
                        for (String requestId : requestIdList) {
                            resultList.add(searchService.getDataPartnerForRequestForDate(currentDate, dataPartner, requestId));
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        return resultList;
    }
}
