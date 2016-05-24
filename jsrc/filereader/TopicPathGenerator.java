package filereader;

import config.DPProperties;
import model.DataPartnerBehaviorVO;
import model.UserCountVO;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kanchan.chowdhary on
 * 5/17/2016.
 */
public class TopicPathGenerator {
    private static final Logger LOGGER = Logger.getLogger(TopicPathGenerator.class);

    private final UserCountGenerator countGenerator;
    private final Map<Integer, DataPartnerBehaviorVO> behaviorMap;

    public TopicPathGenerator() {
        behaviorMap = new HashMap<>();
        countGenerator = new UserCountGenerator();
        generateBehaviorMap();
    }

    private void generateBehaviorMap() {
        try {
            LOGGER.info("Started Reading Topic Attribute Behavior Map File.");
            String behaviorMapFilePath = DPProperties.topicAttributeBehaviorMapFilePath();
            BufferedReader reader = new BufferedReader(new FileReader(behaviorMapFilePath));
            String line;
            int lineCount = 0;
            while ((line = reader.readLine()) != null) {
                lineCount++;
                if (lineCount % 10000 == 0) {
                    LOGGER.info("Read Topic Attribute Behavior Map File: " + lineCount);
                }
                try {
                    String[] values = line.trim().split("\t");
                    int behaviorId = Integer.parseInt(values[5]);
                    String topicPath = values[4].replace(".", " -> ");
                    UserCountVO userCount = countGenerator.getUserCount(behaviorId);
                    DataPartnerBehaviorVO behaviorVO = new DataPartnerBehaviorVO(behaviorId, topicPath, userCount);
                    behaviorMap.put(behaviorId, behaviorVO);
                } catch (RuntimeException e) {
                    LOGGER.info(e);
                }
            }
        } catch (IOException | RuntimeException e) {
            LOGGER.error(e);
        }
        LOGGER.info("Completed Reading Topic Attribute Behavior Map File.");
    }

    public DataPartnerBehaviorVO getBehavior(int behaviorId) {
        DataPartnerBehaviorVO behaviorVO = behaviorMap.get(behaviorId);
        if (behaviorVO == null) {
            behaviorVO = new DataPartnerBehaviorVO(behaviorId, "Topic Not Found for " + behaviorId, 0, 0);
            behaviorMap.put(behaviorId, behaviorVO);
        }
        return behaviorVO;
    }
}
