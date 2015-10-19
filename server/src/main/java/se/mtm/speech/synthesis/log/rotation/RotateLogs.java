package se.mtm.speech.synthesis.log.rotation;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.mtm.speech.synthesis.infrastructure.configuration.LogHome;
import se.mtm.speech.synthesis.infrastructure.configuration.SynthesiseLogsMaxAge;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

class RotateLogs implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(RotateLogs.class);
    private final SynthesiseLogsMaxAge maxAge;
    private final LogHome logHome;

    RotateLogs(SynthesiseLogsMaxAge maxAge, LogHome logHome) {
        this.maxAge = maxAge;
        this.logHome = logHome;
    }

    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            clearOldLogs();
            try {
                Thread.sleep(TimeUnit.HOURS.toMillis(12));
            } catch (InterruptedException e) {
                LOGGER.warn(e.getMessage());
            }
        }
    }

    void clearOldLogs() {
        Collection<File> logFiles = FileUtils.listFiles(logHome.getLogHome(), null, false);
        logFiles.stream().filter(this::tooOld).forEach(File::delete);
    }

    private boolean tooOld(File file) {
        Date input = new Date(file.lastModified());
        LocalDate fileDate = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return fileDate.isBefore(maxAge.getMaxLogAge());
    }
}
