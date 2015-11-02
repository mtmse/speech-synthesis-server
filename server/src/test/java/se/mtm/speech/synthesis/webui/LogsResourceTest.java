package se.mtm.speech.synthesis.webui;

import org.junit.Test;
import se.mtm.speech.synthesis.infrastructure.configuration.LogHome;

import static org.junit.Assert.assertNotNull;

public class LogsResourceTest {

    @Test
    public void get_logs_view() {
        String logHomeName = "build/speech-synthesis-server";

        LogHome logHome = new LogHome(logHomeName);
        LogsResource logsResource = new LogsResource(logHome);

        assertNotNull(logsResource.showAvailableLogs());
    }
}
