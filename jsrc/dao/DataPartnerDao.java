package dao;

import model.DataPartnerDataVO;

import java.util.List;

/**
 * Created by anurag.krishna on
 * 3/16/2016.
 */
public interface DataPartnerDao {

    public List<String> getAllDataPartnersName();

    public List<String> getDateListForDataPartner(String dataPartner);

    public DataPartnerDataVO getAllDataPartnerDataForDataPartner(String dataPartner);

    public List<DataPartnerDataVO> getAllDataPartnerRequestIdsForDate(String date, String dataPartner);

    public DataPartnerDataVO getDataPartnerForRequestIdForDate(String date, String dataPartner, String requestId);
}
