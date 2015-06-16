package se.mtm.speech.synthesis.status;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/logs")
public class LogsResource {
    private final String logHome;

    public LogsResource(String logHome) {
        this.logHome = logHome;
    }

    @GET
    @Produces("text/html;charset=UTF-8")
    public LogsView showAvailableLogs() {
        return new LogsView(logHome);
    }
}
