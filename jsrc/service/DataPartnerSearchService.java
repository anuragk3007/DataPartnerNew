package service;

import model.DataPartnerDataVO;

/**
 * Created by anurag.krishna
 * Date: 3/15/2016.
 */
public interface DataPartnerSearchService {

    public DataPartnerDataVO getDataPartnerForRequest(String dataPartner, String requestId);

    public DataPartnerDataVO getDataPartnerForRequestForDate(String dataPartner, String requestId, String date);

    public DataPartnerDataVO getAllDataPartnerRequest(String dataPartner);

    public DataPartnerDataVO getAllDataPartnerRequestForDate(String dataPartner, String date);
}
