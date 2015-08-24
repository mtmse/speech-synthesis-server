package se.mtm.speech.synthesis.version;

import se.mtm.speech.synthesis.infrastructure.Version;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/version")
@Produces(MediaType.APPLICATION_JSON)
public class VersionResource {
    private Version version;

    @GET
    public Version getVersion() {
        if (version == null) {
            version = new Version();
        }

        return version;
    }
}
