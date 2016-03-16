package servlet;

import model.DataPartnerDataVO;
import org.apache.log4j.Logger;

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


        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        StringBuilder dataPartnerSearchData = new StringBuilder();
        for (DataPartnerDataVO obj : searchQueryResult) {
            dataPartnerSearchData.append(obj.getRequestId()).append('\t').append(obj.getStatus()).append("!");
        }
        response.getWriter().write(dataPartnerSearchData.toString());
    }
}
