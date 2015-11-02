package se.mtm.speech.synthesis.webui;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import se.mtm.speech.synthesis.infrastructure.configuration.LogHome;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class LogsViewTest {

    @Test
    public void get_available_log_files() throws Exception {
        LogHome logHome = new LogHome("build/speech-synthesis-server");
        List<String> expected = prepareLogFiles(logHome);

        LogsView logsView = new LogsView(logHome);

        List<String> actual = logsView.getLogs();

        assertThat(actual, is(expected));
    }

    private List<String> prepareLogFiles(LogHome logHome) throws IOException {
        File logDir = new File(logHome.getHome());
        FileUtils.deleteDirectory(logDir);
        //noinspection ResultOfMethodCallIgnored
        logDir.mkdirs();

        List<String> logFiles = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            String fileName = "filibuster-test-" + i + ".log";
            logFiles.add(fileName);
            FileUtils.touch(new File(logHome.getHome() + fileName));
        }

        return logFiles;
    }
}
