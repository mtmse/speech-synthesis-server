package se.mtm.speech.synthesis.webui;

import se.mtm.speech.synthesis.infrastructure.configuration.LogHome;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/logs")
public class LogsResource {
    private final LogHome logHome;

    public LogsResource(LogHome logHome) {
        this.logHome = logHome;
    }

    @GET
    @Produces("text/html;charset=UTF-8")
    public LogsView showAvailableLogs() {
        return new LogsView(logHome);
    }
}
