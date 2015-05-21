package se.mtm.speech.synthesis.synyhesize;


import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/synthesize")
@Produces(MediaType.APPLICATION_JSON)
public class SynthesizeResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpeechSynthesizer.class);

    private final SpeechSynthesizer synthesizer;

    public SynthesizeResource(SpeechSynthesizer synthesizer) {
        this.synthesizer = synthesizer;
    }

    @GET
    @Timed
    public Paragraph synthesize(@QueryParam("sentence") String sentance) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Received: <" + sentance + ">"); // NOPMD
        }

        String key = "synthesize-" + Thread.currentThread().getId();
        Paragraph paragraph = new Paragraph(key, sentance);

        synthesizer.addParagraph(paragraph);

        while (synthesizer.getParagraph(key) instanceof ParagraphNotReady) {
            pause();
        }

        Paragraph result = synthesizer.getParagraph(key);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Returned: <" + result + ">"); // NOPMD
        }

        return result;
    }

    private void pause() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            LOGGER.warn(e.getMessage());
        }
    }
}
