package cache;

import model.DataPartnerDataVO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by anurag.krishna
 * Date: 3/11/2016.
 */
public class DataPartnerDataCache {

    private Map<String, Map<String, DataPartnerDataVO>> dataPartnerRequestIdDataCache_;

    private void initializeCache() throws IOException {
        Map<String, Map<String, DataPartnerDataVO>> dataPartnerRequestIdDataCache = new HashMap<>();
        File dataPartnerFile = new File("Data/datapartner.txt");
        if (dataPartnerFile.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(dataPartnerFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\\t");
                String dataPartner = values[0];
                String requestId = values[1];
                String[] behaviorsString = values[values.length - 1].split(",");
                HashSet<String> behaviorIds = new HashSet<>();
                behaviorIds.addAll(Arrays.asList(behaviorsString));
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                DataPartnerDataVO dataVO = new DataPartnerDataVO(format.format(new Date()), dataPartner, requestId);
                for (String beh: behaviorIds) {
                    dataVO.addBehavior(Integer.parseInt(beh));
                }

                Map<String, DataPartnerDataVO> requestData = dataPartnerRequestIdDataCache.get(dataPartner);
                if (requestData == null) {
                    requestData = new HashMap<>();
                    dataPartnerRequestIdDataCache.put(dataPartner, requestData);
                }
                requestData.put(requestId, dataVO);
            }
        }
        dataPartnerRequestIdDataCache_ = dataPartnerRequestIdDataCache;
    }

    public DataPartnerDataVO getDataPartnerRequestResult(String dataPartner, String requestId) {
        DataPartnerDataVO dataVO = null;
        Map<String, DataPartnerDataVO> requestIdData = dataPartnerRequestIdDataCache_.get(dataPartner);
        if (requestIdData != null) {
            dataVO = requestIdData.get(requestId);
        }
        return dataVO;
    }
}
