package filereader;

import config.DPProperties;
import model.DataPartnerBehaviorVO;
import org.apache.log4j.Logger;
import utils.MultiMemberGZIPInputStream;

import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kanchan.chowdhary on
 * 5/20/2016.
 */
public class UrlBehaviorListGenerator {
    private static final Logger LOGGER = Logger.getLogger(UrlBehaviorListGenerator.class);

    private final Date logDate;
    private final TopicPathGenerator topicPathGenerator;
    private final Map<Long, Set<DataPartnerBehaviorVO>> urlHashBehaviorMap;

    public UrlBehaviorListGenerator(Date logDate) {
        this.logDate = logDate;
        topicPathGenerator = new TopicPathGenerator();
        urlHashBehaviorMap = new HashMap<>();
        generateUrlHashBehaviorMap();
    }

    private void generateUrlHashBehaviorMap() {
        try {
            System.out.println("Started Reading Behavior Description File");
            String line;
            int count = 0;
            Pattern urlIdBehaviorLogPattern = Pattern.compile("(-?[\\d]+),(.+)");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new MultiMemberGZIPInputStream(
                    new FileInputStream(getBehaviorDescriptionFile()))))) {
                while ((line = reader.readLine()) != null) {
                    try {
                        count++;
                        if (count % 100000 == 0) {
                            LOGGER.info("Line read from behavior log file: " + count);
                        }
                        Matcher lineMatcher = urlIdBehaviorLogPattern.matcher(line.trim());
                        if (lineMatcher.matches()) {
                            long urlHash = Math.abs(Long.parseLong(lineMatcher.group(1)));
                            String[] behaviorIdList = lineMatcher.group(2).split(",");
                            int behaviorCount = Integer.parseInt(behaviorIdList[1]);
                            if (behaviorCount > 0) {
                                Set<DataPartnerBehaviorVO> behaviorList = new HashSet<>();
                                for (int i = 0; i < behaviorCount; i++) {
                                    behaviorList.add(topicPathGenerator.getBehavior(Integer.parseInt(behaviorIdList[2 + i])));
                                }
                                urlHashBehaviorMap.put(urlHash, behaviorList);
                            }
                        }
                    } catch (RuntimeException e) {
                        LOGGER.error(e);
                    }
                }
            } catch (Throwable e) {
                LOGGER.error(e);
            }
        } catch (RuntimeException e) {
            LOGGER.error(e);
        }
        LOGGER.info("Completed Reading Behavior Description File");
    }

    @SuppressWarnings("ConstantConditions")
    private File getBehaviorDescriptionFile() {
        SimpleDateFormat format = new SimpleDateFormat(DPProperties.datedFileStructure());
        String baseDir = DPProperties.urlLogBaseDirectory()+format.format(logDate);
        File behaviorDescriptionFile = null;
        for (File file : new File(baseDir).listFiles()) {
            if (file.getName().startsWith(DPProperties.behaviorDescFilePrefix())) {
                behaviorDescriptionFile = file;
                break;
            }
        }
        return behaviorDescriptionFile;
    }

    public Set<DataPartnerBehaviorVO> getBehaviorList(long urlHash) {
        Set<DataPartnerBehaviorVO> behaviorList = urlHashBehaviorMap.get(Math.abs(urlHash));
        if (behaviorList == null) {
            behaviorList = new HashSet<>();
        }
        return behaviorList;
    }
}
