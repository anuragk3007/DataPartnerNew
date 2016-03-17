package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.DataPartnerDataVO;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import service.DataPartnerSearchService;
import service.DataPartnerSearchServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by kanchan.chowdhary on
 * 3/15/2016.
 */
public class RequestHandler extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(RequestHandler.class);

    private DataPartnerSearchService searchService;

    public RequestHandler() {
        searchService = new DataPartnerSearchServiceImpl();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Enumeration paramNameList = request.getParameterNames();
        String dataPartnerName = "";
        Set<DataPartnerDataVO> searchQueryResult = new TreeSet<>();
        List<String> requestIdList = new ArrayList<>();
        String requestType = "";
        while (paramNameList.hasMoreElements()) {
            String parameterName = (String) paramNameList.nextElement();
            String[] paramValueList = request.getParameterValues(parameterName);
            if (paramValueList.length != 0) {
                if (parameterName.equalsIgnoreCase("dataPartner")) {
                    dataPartnerName = paramValueList[0];
                } else if (parameterName.equalsIgnoreCase("requestIdList")) {
                    requestIdList.addAll(Arrays.asList(paramValueList[0].split("\n")));
                } else if (parameterName.equalsIgnoreCase("requestType")) {
                    requestType = paramValueList[0];
                }
            }
        }
        System.out.println("RequestType: " + requestType);
        if (requestType.equalsIgnoreCase("query")) {
            String date = "16-03-2016";

            for (String requestId : requestIdList) {
                DataPartnerDataVO dataVO = searchService.getDataPartnerForRequestForDate(dataPartnerName, requestId, date);
                if (dataVO != null) {
                    searchQueryResult.add(dataVO);
                } else {
                    searchQueryResult.add(new DataPartnerDataVO(dataPartnerName, requestId));
                }
            }


            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Gson gson = new Gson();
            JSONArray jsonResultList = new JSONArray();
            ObjectMapper mapper = new ObjectMapper();
            for (DataPartnerDataVO result : searchQueryResult) {
                System.out.println(result.getRequestId());
                String jsonObject = mapper.writeValueAsString(result);
                jsonResultList.put(jsonObject);
            }
            JsonElement jsonElement = gson.toJsonTree(jsonResultList);
            JsonObject responseObject = new JsonObject();
            responseObject.addProperty("success", true);
            responseObject.add("resultList", jsonElement);
            response.getWriter().print(responseObject);
        } else if (requestType.equalsIgnoreCase("download")) {
            System.out.println("Download link clicked");
            File resultFile = new File("dataPartnerUI/data/RequestFinderResult.csv");
            resultFile.delete();
            resultFile.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile));
            try {
                writer.write("Request Id,Remarks,Behavior List\n");
                for (DataPartnerDataVO obj : searchQueryResult) {
                    writer.write(obj.toString());
                    writer.flush();
                }
            } finally {
                writer.close();
            }
            response.setContentType("csv/application");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(resultFile);
        }
    }

    public TreeSet<DataPartnerDataVO> getQueryResult() {
        TreeSet<DataPartnerDataVO> result = new TreeSet<>();
        DataPartnerDataVO obj = new DataPartnerDataVO("adobe", "adb2");
        obj.addBehavior("adbeh21");
        obj.addBehavior("adbeh22");
        result.add(obj);
        obj = new DataPartnerDataVO("adobe", "adb0");
        result.add(obj);
        obj = new DataPartnerDataVO("adobe", "adb1");
        obj.addBehavior("adbeh11");
        result.add(obj);
        obj = new DataPartnerDataVO("adobe", "adb3");
        result.add(obj);
        return result;
    }

}
