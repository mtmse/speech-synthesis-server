package se.mtm.speech.synthesis.infrastructure;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LogNameTest {
    @Test
    public void return_first_log_file_name() throws Exception {
        String logHome = createLogDir();
        String today = getToday();

        String expected = "filibuster-" + today + "-0001.log";

        String actual = LogName.getLogFileName(logHome);

        assertThat(actual, is(expected));
    }

    @Test
    public void return_second_log_file_name() throws Exception {
        String logHome = createLogDir();
        String today = getToday();

        File previousLog = new File(logHome + "/" + "filibuster-" + today + "-0001.log");
        FileUtils.touch(previousLog);

        String expected = "filibuster-" + today + "-0002.log";

        String actual = LogName.getLogFileName(logHome);

        assertThat(actual, is(expected));
    }

    private String getToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM", Locale.ENGLISH);
        return sdf.format(new Date());
    }

    private String createLogDir() throws IOException {
        String logHome = "build/speech-synthesis-server";
        File logDir = new File(logHome);
        FileUtils.deleteDirectory(logDir);
        //noinspection ResultOfMethodCallIgnored
        logDir.mkdirs();
        return logHome;
    }
}
