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
    private final long defaultTimeout;

    public SynthesizeResource(SpeechSynthesizer synthesizer, long defaultTimeout) {
        this.synthesizer = synthesizer;
        this.defaultTimeout = defaultTimeout;
    }

    @GET
    @Timed
    // todo add an optional parameter, ttl for the paragraph
    public ParagraphInterface synthesize(@QueryParam("sentence") String sentance) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Received: <" + sentance + ">"); // NOPMD
        }

        String key = "synthesize-" + Thread.currentThread().getId();
        ParagraphReady paragraphReady = new ParagraphReady(key, sentance);

        long timeout = System.currentTimeMillis() + defaultTimeout;

        synthesizer.addParagraph(paragraphReady);

        while (synthesizer.isParagraphReady(key) instanceof ParagraphNotReady) {
            if (System.currentTimeMillis() > timeout) {
                return new ParagraphTimedOutSpelling();
            }

            pause();
        }

        ParagraphInterface result = synthesizer.popParagraph(key);

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
