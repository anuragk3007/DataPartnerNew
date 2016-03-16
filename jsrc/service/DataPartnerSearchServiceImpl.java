package service;

import dao.DataPartnerDao;
import dao.DataPartnerDaoImpl;
import model.DataPartnerDataVO;

import java.util.List;

/**
 * Created by anurag.krishna
 * Date: 3/15/2016.
 */
public class DataPartnerSearchServiceImpl implements DataPartnerSearchService {
    private DataPartnerDao dataPartnerDao;

    public DataPartnerSearchServiceImpl() {
        dataPartnerDao = new DataPartnerDaoImpl();
    }

    @Override
    public List<DataPartnerDataVO> getDataPartnerForRequest(String dataPartner, String requestId) {
        return dataPartnerDao.getDataPartnerForRequestId(dataPartner, requestId);
    }

    @Override
    public DataPartnerDataVO getDataPartnerForRequestForDate(String dataPartner, String requestId, String date) {
        return null;
    }

    @Override
    public DataPartnerDataVO getAllDataPartnerRequest(String dataPartner) {
        return null;
    }

    @Override
    public DataPartnerDataVO getAllDataPartnerRequestForDate(String dataPartner, String date) {
        return null;
    }
}
