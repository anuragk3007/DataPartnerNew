package filereader;

import com.csvreader.CsvReader;
import properties.DPProperties;
import model.UserCountVO;
import org.apache.log4j.Logger;
import utils.DataPartnerConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kanchan.chowdhary on
 * 5/17/2016.
 */
public class UserCountGenerator {
    private static final Logger LOGGER = Logger.getLogger(UserCountGenerator.class);
    private final Map<Integer, UserCountVO> behaviorIdCountMap;

    public UserCountGenerator() {
        behaviorIdCountMap = new HashMap<>();
        generateMap();
    }

    private void generateMap() {
        LOGGER.info("Started Reading Behavior Topic Unique Users File.");
        try {
            String userCountFilePath = DPProperties.behaviorTopicUniqueUsersFilePath();
            CsvReader reader = new CsvReader(userCountFilePath);
            int lineCount = 0;
            while (reader.readRecord()) {
                try {
                    lineCount++;
                    if (lineCount % 100000 == 0) {
                        LOGGER.info("Read Behavior Topic Unique Users File: " + lineCount);
                    }
                    int geoId = Integer.parseInt(reader.get(3));
                    if (geoId == DataPartnerConstants.US_GEO_ID || geoId == DataPartnerConstants.UK_GEO_ID) {
                        int behaviorId = Integer.parseInt(reader.get(1));
                        UserCountVO userCount = behaviorIdCountMap.get(behaviorId);
                        if (userCount == null) {
                            userCount = new UserCountVO();
                            behaviorIdCountMap.put(behaviorId, userCount);
                        }
                        int count = Integer.parseInt(reader.get(7));
                        userCount.setCount(geoId, count);
                    }
                } catch (RuntimeException e) {
                    LOGGER.error(e);
                }
            }
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        LOGGER.info("Completed Reading Behavior Topic Unique Users File.");
    }

    public UserCountVO getUserCount(int behaviorId) {
        UserCountVO userCount = behaviorIdCountMap.get(behaviorId);
        if (userCount == null) {
            userCount = new UserCountVO();
            behaviorIdCountMap.put(behaviorId, userCount);
        }
        return userCount;
    }
}
