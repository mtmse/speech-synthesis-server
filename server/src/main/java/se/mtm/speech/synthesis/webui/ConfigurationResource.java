package se.mtm.speech.synthesis.webui;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/configuration")
public class ConfigurationResource {
    public ConfigurationResource() {
    }

    @GET
    @Produces("text/html;charset=UTF-8")
    public ConfigurationView showConfigurationView() {
        return new ConfigurationView();
    }
}
