package se.mtm.speech.synthesis.status;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class LogsView extends CommonView {
    private final String logHome;

    public LogsView(String logHome) {
        super("logs.mustache", Charset.forName("UTF-8"));
        this.logHome = logHome;
    }

    public List<String> getLogs() throws IOException {
        List<String> logFileNames = new LinkedList<>();

        File logDir = new File(logHome);
        String[] extensions = {"log"};

        Collection<File> logFiles = FileUtils.listFiles(logDir, extensions, false);

        for (File logFile : logFiles) {
            String name = logFile.getName();
            logFileNames.add(name);
        }

        return logFileNames;
    }
}
