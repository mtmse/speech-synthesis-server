package se.mtm.speech.synthesis.log.rotation;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import se.mtm.speech.synthesis.infrastructure.configuration.LogHome;
import se.mtm.speech.synthesis.infrastructure.configuration.SynthesiseLogsMaxAge;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RotateLogsTest {
    @Rule
    public TemporaryFolder logDir = new TemporaryFolder();

    private int logFiles;
    private LogHome logHome;


    @Before
    public void setUp() throws Exception {
        logFiles = 15;
        createSampleLogFiles(logFiles);
        logHome = new LogHome(logDir.getRoot().getAbsolutePath());
    }

    @Test
    public void delete_old_files() throws Exception {
        Collection<File> initialLogFiles = getLogFiles();
        assertThat(initialLogFiles.size(), is(logFiles));

        int expectedFileCount = 11;
        SynthesiseLogsMaxAge maxAge = new SynthesiseLogsMaxAge(10);
        RotateLogs rotateLogs = new RotateLogs(maxAge, logHome);

        rotateLogs.clearOldLogs();

        Collection<File> actualLogFiles = getLogFiles();
        assertThat(actualLogFiles.size(), is(expectedFileCount));
    }

    private void createSampleLogFiles(int logfiles) throws IOException {
        for (int days = 0; days < logfiles; days++) {
            File createdFile = logDir.newFile(days + "logfile.log");
            LocalDate modificationDate = LocalDate.now().minusDays(days);
            Date date = Date.from(modificationDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            //noinspection ResultOfMethodCallIgnored
            createdFile.setLastModified(date.getTime());
        }
    }

    private Collection<File> getLogFiles() {
        return FileUtils.listFiles(logDir.getRoot(), null, false);
    }
}
