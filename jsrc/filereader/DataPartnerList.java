package filereader;

import config.DPProperties;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanchan.chowdhary on
 * 5/21/2016.
 */
public class DataPartnerList {
    private static final Logger LOGGER = Logger.getLogger(DataPartnerList.class);

    private final List<String> dataPartnerList;

    public DataPartnerList() {
        dataPartnerList = new ArrayList<>();
        generateList();
    }

    private void generateList() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(DPProperties.dataPartnerListFile()));
            String line;
            while ((line=reader.readLine())!=null) {
                dataPartnerList.add(line.trim());
            }
        } catch (IOException e){
            LOGGER.error(e);
        }
    }

    public List<String> getList() {
        return dataPartnerList;
    }
}
