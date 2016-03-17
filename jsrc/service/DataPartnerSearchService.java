package service;

import model.DataPartnerDataVO;

import java.util.List;

/**
 * Created by anurag.krishna
 * Date: 3/15/2016.
 */
public interface DataPartnerSearchService {

    public List<DataPartnerDataVO> getDataPartnerForRequest(String dataPartner, String requestId);

    public DataPartnerDataVO getDataPartnerForRequestForDate(String dataPartner, String requestId, String date);

    public List<DataPartnerDataVO> getAllDataPartnerRequest(String dataPartner);

    public List<DataPartnerDataVO> getAllDataPartnerRequestForDate(String dataPartner, String date);
}
