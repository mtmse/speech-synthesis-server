package se.mtm.speech.synthesis.infrastructure;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import se.mtm.speech.synthesis.infrastructure.configuration.LogHome;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LogNameTest {
    private static final String FILIBUSTER = "filibuster-";

    @Test
    public void return_first_log_file_name() throws Exception {
        LogHome logHome = createLogDir();
        String today = getToday();

        String expected = FILIBUSTER + today + "-0001.log";

        String actual = LogName.getLogFileName(logHome);

        assertThat(actual, is(expected));
    }

    @Test
    public void return_second_log_file_name() throws Exception {
        LogHome logHome = createLogDir();
        String today = getToday();

        File previousLog = new File(logHome + "/" + FILIBUSTER + today + "-0001.log");
        FileUtils.touch(previousLog);

        String expected = FILIBUSTER + today + "-0002.log";

        String actual = LogName.getLogFileName(logHome);

        assertThat(actual, is(expected));
    }

    @Test
    public void return_first_filibuster_log_file_name() throws Exception {
        LogHome logHome = createLogDir();
        String today = getToday();

        File previousLog = new File(logHome + "/" + "speech-synthesis.log");
        FileUtils.touch(previousLog);

        String expected = FILIBUSTER + today + "-0001.log";

        String actual = LogName.getLogFileName(logHome);

        assertThat(actual, is(expected));
    }

    private String getToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM", Locale.ENGLISH);
        return sdf.format(new Date());
    }

    private LogHome createLogDir() throws IOException {
        String logHome = "build/speech-synthesis-server";
        File logDir = new File(logHome);
        FileUtils.deleteDirectory(logDir);
        //noinspection ResultOfMethodCallIgnored
        logDir.mkdirs();

        return new LogHome(logHome);
    }
}
