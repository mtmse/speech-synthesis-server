package se.mtm.speech.synthesis.status;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class LogsResourceTest {

    @Test
    public void get_logs_view() {
        String logHome = "build/speech-synthesis-server";

        LogsResource logsResource = new LogsResource(logHome);

        assertNotNull(logsResource.showAvailableLogs());
    }
}
