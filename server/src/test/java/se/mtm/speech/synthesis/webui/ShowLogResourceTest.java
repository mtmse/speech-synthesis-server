package se.mtm.speech.synthesis.webui;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import se.mtm.speech.synthesis.infrastructure.configuration.LogHome;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ShowLogResourceTest {
    @Test
    public void get_log_file_content() throws Exception {
        LogHome logHome = new LogHome("build/speech-synthesis-server/");
        String logName = "faked.log";
        String expected = prepareLog(logHome, logName);
        ShowLogResource resource = new ShowLogResource(logHome);

        String actual = resource.showLog(logName);

        assertThat(actual, is(expected));
    }

    private String prepareLog(LogHome logHome, String logName) throws Exception {
        File logDir = new File(logHome.getHome());
        FileUtils.deleteDirectory(logDir);
        //noinspection ResultOfMethodCallIgnored
        logDir.mkdirs();

        String content = "faked log content";

        File logFile = new File(logHome.getHome() + logName);
        FileUtils.write(logFile, content, "UTF-8");

        return content;
    }
}
