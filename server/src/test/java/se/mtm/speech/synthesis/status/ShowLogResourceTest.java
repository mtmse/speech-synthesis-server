package se.mtm.speech.synthesis.status;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ShowLogResourceTest {
    @Test
    public void get_log_file_content() throws Exception {
        String logHome = "build/speech-synthesis-server/";
        String logName = "faked.log";
        String expected = prepareLog(logHome, logName);
        ShowLogResource resource = new ShowLogResource(logHome);

        String actual = resource.showLog(logName);

        assertThat(actual, is(expected));
    }

    private String prepareLog(String logHome, String logName) throws Exception {
        File logDir = new File(logHome);
        FileUtils.deleteDirectory(logDir);
        //noinspection ResultOfMethodCallIgnored
        logDir.mkdirs();

        String content = "faked log content";

        File logFile = new File(logHome + "/" + logName);
        FileUtils.write(logFile, content, "UTF-8");

        return content;
    }
}
