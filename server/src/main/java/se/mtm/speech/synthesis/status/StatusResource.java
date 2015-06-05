package se.mtm.speech.synthesis.status;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/")
public class StatusResource {

    @GET
    @Produces("text/html;charset=UTF-8")
    public StatusView showSpeechServerStatus() {
        return new StatusView();
    }
}
