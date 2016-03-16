package dao;

import model.DataPartnerDataVO;

import java.util.List;

/**
 * Created by anurag.krishna on 3/16/2016.
 */
public interface DataPartnerDao {

    public List<DataPartnerDataVO> getDataPartnerForRequestId(String dataPartner, String requestId);

    public DataPartnerDataVO getDataPartnerForRequestIdForDate(String dataPartner, String requestId, String date);

    public List<DataPartnerDataVO> getAllDataPartnerRequestIds(String dataPartner);

    public List<DataPartnerDataVO> getAllDataPartnerRequestIdsForDate(String dataPartner, String date);
}
