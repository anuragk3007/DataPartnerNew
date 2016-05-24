package config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by kanchan.chowdhary on
 * 5/3/2016.
 */
public class DPProperties extends Properties {
    private static final DPProperties INSTANCE = new DPProperties();
    private static final Map<Object, Object> properties = new HashMap<>(INSTANCE);

    private DPProperties() {
        try {
            load(DPProperties.class.getClassLoader().getResourceAsStream("/resources/dp.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getDPProperty(String key) {
        return (String) properties.get(key);
    }

    public static String rootDirectory() {
        return getDPProperty("rootDirectory");
    }

    public static String resultFileName() {
        return getDPProperty("resultFileName");
    }

    public static String relativeResultDirPath() {
        return getDPProperty("resultFileDirectory");
    }

    public static String relativeResultFilePath() {
        return relativeResultDirPath() + resultFileName();
    }

    public static String absoluteResultDirPath() {
        return rootDirectory() + relativeResultDirPath();
    }

    public static String absoluteResultFilePath() {
        return absoluteResultDirPath() + resultFileName();
    }

    public static String behaviorTopicUniqueUsersFilePath() {
        return getDPProperty("behaviorTopicUniqueUsers");
    }

    public static String topicAttributeBehaviorMapFilePath() {
        return getDPProperty("topicAttributeBehaviorMapFile");
    }

    public static String urlLogBaseDirectory() {
        return getDPProperty("urlLogBaseDirectory");
    }

    public static String urlLogFilePrefix() {
        return getDPProperty("urlLogFilePrefix");
    }

    public static String behaviorDescFilePrefix() {
        return getDPProperty("behaviorDescFilePrefix");
    }

    public static String datedFileStructure() {
        return getDPProperty("datedFileStructure");
    }

    public static String dataPartnerListFile() {
        return getDPProperty("dataPartnerListFile");
    }

    public static void main(String[] args) {
        System.out.println(rootDirectory());
        System.out.println(relativeResultDirPath());
        System.out.println(relativeResultFilePath());
        System.out.println(absoluteResultFilePath());
        System.out.println(behaviorTopicUniqueUsersFilePath());
        System.out.println(topicAttributeBehaviorMapFilePath());
        System.out.println(urlLogBaseDirectory());
        System.out.println(urlLogFilePrefix());
        System.out.println(behaviorDescFilePrefix());
        System.out.println(datedFileStructure());
        System.out.println(dataPartnerListFile());
    }
}
