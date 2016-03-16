package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.DataPartnerDataVO;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by kanchan.chowdhary on
 * 3/15/2016.
 */
public class RequestHandler extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(RequestHandler.class);
    private String dataPartnerName;
    private List<String> requestIdList;
    private Set<DataPartnerDataVO> searchQueryResult = new TreeSet<>();
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Enumeration paramNameList = request.getParameterNames();
        requestIdList = new ArrayList<>();
        while (paramNameList.hasMoreElements()) {
            String parameterName = (String) paramNameList.nextElement();
            String[] paramValueList = request.getParameterValues(parameterName);
            if (paramValueList.length == 0) {
                if (parameterName.equalsIgnoreCase("dataPartner")) {
                    dataPartnerName = paramValueList[0];
                } else if (parameterName.equalsIgnoreCase("requestIds")) {
                    requestIdList.addAll(Arrays.asList(paramValueList[0].split("\n")));
                }
            }
        }
        /*Place a call to service for elastic search dataPartnerName and requestIdList*/


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        JSONArray jsonResultList = new JSONArray();
        ObjectMapper mapper = new ObjectMapper();
        for (DataPartnerDataVO result: searchQueryResult) {
            String jsonObject = mapper.writeValueAsString(result);
            jsonResultList.put(jsonObject);
        }
        JsonElement jsonElement = gson.toJsonTree(jsonResultList);
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("success", true);
        responseObject.add("resultList", jsonElement);
        response.getWriter().print(responseObject);
    }
}
