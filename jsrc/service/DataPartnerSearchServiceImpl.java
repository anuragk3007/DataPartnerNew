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
    public List<String> getAllDataPartnersName() {
        return dataPartnerDao.getAllDataPartnersName();
    }

    @Override
    public List<String> getDateListForDataPartner(String dataPartner) {
        return dataPartnerDao.getDateListForDataPartner(dataPartner);
    }

    @Override
    public DataPartnerDataVO getAllDataPartnerDataForDataPartner(String date) {
        return dataPartnerDao.getAllDataPartnerDataForDataPartner(date);
    }

    @Override
    public List<DataPartnerDataVO> getAllDataPartnerRequestForDate(String date, String dataPartner) {
        return dataPartnerDao.getAllDataPartnerRequestIdsForDate(date, dataPartner);
    }

    @Override
    public DataPartnerDataVO getDataPartnerForRequestForDate(String date, String dataPartner, String requestId) {
        return dataPartnerDao.getDataPartnerForRequestIdForDate(date, dataPartner, requestId);
    }
}
