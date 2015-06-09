package se.mtm.speech.synthesis.status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.mtm.speech.synthesis.synyhesize.SpeechSynthesizer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/invalidate")
@Produces("text/html;charset=UTF-8")
public class InvalidateFilibusterResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvalidateFilibusterResource.class);

    private final SpeechSynthesizer synthesizer;

    public InvalidateFilibusterResource(SpeechSynthesizer synthesizer) {
        this.synthesizer = synthesizer;
    }

    @GET
    public StatusView invalidate() {
        LOGGER.info("Invalidate all Filibusters");

        synthesizer.invalidate();

        LOGGER.info("All Filibusters was invalidated");

        return new StatusView();
    }
}
