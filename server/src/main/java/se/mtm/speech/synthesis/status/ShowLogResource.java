package se.mtm.speech.synthesis.status;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.mtm.speech.synthesis.infrastructure.configuration.LogHome;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.File;
import java.io.IOException;

@Path("/showLog/{logFile}")
public class ShowLogResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShowLogResource.class);
    private final LogHome logHome;

    public ShowLogResource(LogHome logHome) {
        this.logHome = logHome;
    }

    @GET
    @Produces("text/plain;charset=UTF-8")
    public String showLog(@PathParam("logFile") String logFileName) throws IOException {
        String message = "View log file <" + logFileName + ">";
        LOGGER.info(message);

        File logFileContent = new File(logHome.getHome() + logFileName);

        return FileUtils.readFileToString(logFileContent);
    }
}
