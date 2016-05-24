package service;

import model.DataPartnerDataVO;

import java.util.List;

/**
 * Created by anurag.krishna
 * Date: 3/15/2016.
 */
public interface DataPartnerSearchService {

    public List<String> getAllDataPartnersName();

    public List<String> getDateListForDataPartner(String dataPartner);

    public DataPartnerDataVO getAllDataPartnerDataForDataPartner(String dataPartner);

    public List<DataPartnerDataVO> getAllDataPartnerRequestForDate(String date, String dataPartner);

    public DataPartnerDataVO getDataPartnerForRequestForDate(String date, String dataPartner, String requestId);
}
