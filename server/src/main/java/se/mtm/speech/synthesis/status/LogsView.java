package se.mtm.speech.synthesis.status;

import org.apache.commons.io.FileUtils;
import se.mtm.speech.synthesis.infrastructure.configuration.LogHome;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LogsView extends CommonView {
    private final LogHome logHome;

    public LogsView(LogHome logHome) {
        super("logs.mustache", Charset.forName("UTF-8"));
        this.logHome = logHome;
    }

    public List<String> getLogs() throws IOException {
        List<String> logFileNames = new LinkedList<>();

        File logDir = new File(logHome.getHome());
        String[] extensions = {"log"};

        Collection<File> logFiles = FileUtils.listFiles(logDir, extensions, false);

        for (File logFile : logFiles) {
            String name = logFile.getName();
            logFileNames.add(name);
        }

        Collections.sort(logFileNames);

        return logFileNames;
    }
}
