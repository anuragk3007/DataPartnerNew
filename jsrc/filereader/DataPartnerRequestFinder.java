package filereader;

import model.DataPartnerDataVO;
import org.apache.log4j.Logger;
import utils.MultiMemberGZIPInputStream;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kanchan.chowdhary on 3/10/2016.
 */
public class DataPartnerRequestFinder {
    private static final Logger LOGGER = Logger.getLogger(DataPartnerRequestFinder.class);

    private final List<String> dataPartnerList;
    private final Map<Long, DataPartnerDataVO> requestList;

    public DataPartnerRequestFinder(ArrayList dataPartnerList) {
        requestList = new ConcurrentHashMap<>();
        this.dataPartnerList = new ArrayList<>();
        this.dataPartnerList.addAll(dataPartnerList);
    }

    private String getDateDirectoryStructure(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("Current Date: " + format.format(date));
        String[] dateToken = format.format(date).split("/");
        return dateToken[2] + "/" + dateToken[2] + "-" + dateToken[1] + "/" + dateToken[2] + "-" + dateToken[1] + "-" + dateToken[0] + "/";
    }

    public void readUrlLogAndBehaviorFile(Date logDate) throws IOException {
        String baseDir = "/mnt/prodBackup/tf/real/logDir/urlCacheServer/ucsLogDir/ucs0/urlLog/";
//        String baseDir = BIProperties.tfrSMDataDirectory() + "cnops/DataPartner/";
        String urlLogDatedDirPath = baseDir + getDateDirectoryStructure(logDate);
        File[] fileList = new File(urlLogDatedDirPath).listFiles();
        File urlLogFile = null;
        File behaviorFile = null;
        for (File file : fileList) {
            if (file.getName().startsWith("URLID_URL")) {
                urlLogFile = file;
            } else if (file.getName().startsWith("URLID_BehaviorDesc")) {
                behaviorFile = file;
            }
        }
        if (urlLogFile == null || behaviorFile == null) {
            LOGGER.info("Url log and/or behavior file not found");
            return;
        }

        LOGGER.info("Reading url log file");

        BufferedReader reader = new BufferedReader(new InputStreamReader(new MultiMemberGZIPInputStream(new FileInputStream(urlLogFile))));
        String line;
        int lineCount = 0;
        Pattern urlIdUrlLogPattern = Pattern.compile("(-?[\\d]+),[A-Z],(.+)");
//        Pattern requestPattern =Pattern.compile("site=(" + dataPartnerList.toString().replaceAll("[\\[\\]]", "").replace(", ", "|") + ")\\|(.+)");
        Pattern requestPattern = Pattern.compile("site=(" + dataPartnerList.toString().replaceAll("[\\[\\]]", "").replace(", ", "|") + ")\\|[^=]+=([^|]+)\\|");
        try {
            while ((line = reader.readLine()) != null) {
                try {
                    if (lineCount % 10000 == 0) {
                        LOGGER.info("Line read from url log file: " + lineCount);
                    }
                    lineCount++;
                    Matcher lineMatcher = urlIdUrlLogPattern.matcher(line.trim());
                    if (lineMatcher.matches()) {
                        final Long urlHash = Long.parseLong(lineMatcher.group(1));
                        String requestUrl = lineMatcher.group(2);
                        final Matcher matcher = requestPattern.matcher(requestUrl);
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (matcher.matches()) {
                                    String dataPartnerName = matcher.group(1);
                                    String requestId = matcher.group(2);
                                    requestList.put(urlHash, new DataPartnerDataVO(dataPartnerName, requestId));
                                }
                            }
                        });
                        thread.start();
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }
        LOGGER.info("Completed Reading UrlLog File");
        LOGGER.info("Reading behavior File");
        reader = new BufferedReader(new InputStreamReader(new MultiMemberGZIPInputStream(new FileInputStream(behaviorFile))));
        lineCount = 0;
        Pattern urlIdBehaviorLogPattern = Pattern.compile("(-?[\\d]+),(.+)");
        try {
            while ((line = reader.readLine()) != null) {
                try {
                    if (lineCount % 10000 == 0) {
                        LOGGER.info("Line read from behavior log file: " + lineCount);
                    }
                    lineCount++;
                    Matcher lineMatcher = urlIdBehaviorLogPattern.matcher(line.trim());
                    if (lineMatcher.matches()) {
                        long urlHash = Long.parseLong(lineMatcher.group(1));
                        String[] behaviorList = lineMatcher.group(2).split(",");
                        DataPartnerDataVO obj = requestList.get(urlHash);
                        if (obj != null) {
                            int behaviorCount = Integer.parseInt(behaviorList[1]);
                            for (int i = 0; i < behaviorCount; i++) {
                                obj.addBehavior(behaviorList[2 + i]);
                            }
                        }
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }
    }

    public void writeResult(String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        for (Map.Entry<Long, DataPartnerDataVO> entry : requestList.entrySet()) {
            writer.write(entry.getValue().toString() + "\n");
            writer.flush();
        }
        writer.close();
    }

    public static void main(String[] args) throws IOException {
        ArrayList<String> list = new ArrayList<>(Arrays.asList("ibehavior!liveramp!bluekai!adobe!lotame!datalogix!ihg!enterprise".split("!")));
        DataPartnerRequestFinder obj = new DataPartnerRequestFinder(list);
        obj.readUrlLogAndBehaviorFile(new Date(System.currentTimeMillis()));
        //obj.writeResult(BIProperties.tfrSMDataDirectory() + "cnops/DataPartner/dataPartnerIntermediateData.txt");
        System.out.println(new DataPartnerRequestFinder(new ArrayList()).getDateDirectoryStructure(new Date(System.currentTimeMillis())));
    }
}
