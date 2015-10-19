package se.mtm.speech.synthesis.log.rotation;

import io.dropwizard.lifecycle.Managed;
import se.mtm.speech.synthesis.infrastructure.configuration.LogHome;
import se.mtm.speech.synthesis.infrastructure.configuration.SynthesiseLogsMaxAge;

public class LogRotator implements Managed {
    private final RotateLogs rotator;

    public LogRotator(SynthesiseLogsMaxAge maxAge, LogHome logHome) {
        rotator = new RotateLogs(maxAge, logHome);
    }

    @Override
    public void start() throws Exception {
        Thread thread = new Thread(rotator);
        thread.start();
    }

    @Override
    public void stop() throws Exception {
        // unimplemented, don't think there is any good reason to do any house keeping before terminating
    }
}
