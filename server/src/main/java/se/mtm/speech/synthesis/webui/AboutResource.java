package se.mtm.speech.synthesis.webui;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/about")
public class AboutResource {

    @GET
    @Produces("text/html;charset=UTF-8")
    public AboutView showAboutView() {
        return new AboutView();
    }
}
