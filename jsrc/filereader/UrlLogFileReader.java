package filereader;

import properties.DPProperties;
import org.apache.log4j.Logger;
import utils.MultiMemberGZIPInputStream;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kanchan.chowdhary on
 * 3/10/2016.
 */
public class UrlLogFileReader extends Thread{
    private static final Logger LOGGER = Logger.getLogger(UrlLogFileReader.class);

    private final Date logDate;
    private final BlockingQueue  sharedQueue;

    public UrlLogFileReader(Date logDate, BlockingQueue blockingQueue) {
        this.logDate = logDate;
        sharedQueue = blockingQueue;
    }

    public void run(){
        generateData();
    }
    @SuppressWarnings("unchecked")
    private void generateData() {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new MultiMemberGZIPInputStream(new FileInputStream(getUrlLogFile()))))) {
            LOGGER.info("Started Reading UrlLog File");
            List<String> dataPartnerList = new DataPartnerList().getList();
            Pattern urlIdUrlLogPattern = Pattern.compile("(-?[\\d]+),[A-Z],site=("+ dataPartnerList.toString()
                    .replaceAll("[\\[\\]]", "").replace(", ", "|") + ")\\|[^=]+=([^|]+)\\|");

            String line;
            int lineCount = 0;
            while ((line=reader.readLine())!=null) {
                try {
                    lineCount++;
                    if (lineCount % 100000 == 0) {
                        LOGGER.info("Line read from url log file: " + lineCount);
                    }
                    Matcher lineMatcher = urlIdUrlLogPattern.matcher(line.trim());
                    if (lineMatcher.matches()) {
                        List<String> request = new ArrayList<>();
                        request.add(lineMatcher.group(1));
                        request.add(lineMatcher.group(2));
                        request.add(lineMatcher.group(3));
                        sharedQueue.put(request);
                    }
                } catch (Throwable e) {
                    LOGGER.error(e);
                }
            }
        } catch (IOException | RuntimeException e) {
            LOGGER.error(e);
        }
        LOGGER.info("Completed reading url log");
    }

    @SuppressWarnings("ConstantConditions")
    private File getUrlLogFile() {
        SimpleDateFormat format = new SimpleDateFormat(DPProperties.datedFileStructure());
        String baseDir = DPProperties.urlLogBaseDirectory() + format.format(logDate);
        File urlLogFile = null;
        for (File file : new File(baseDir).listFiles()) {
            if (file.getName().startsWith(DPProperties.urlLogFilePrefix())) {
                urlLogFile = file;
                break;
            }
        }
        return urlLogFile;
    }
}
