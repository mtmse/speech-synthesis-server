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
    private long idleTime;
    private final long defaultTimeout;

    public SynthesizeResource(SpeechSynthesizer synthesizer, long defaultTimeout, long idleTime) {
        this.synthesizer = synthesizer;
        this.idleTime = idleTime;
        this.defaultTimeout = defaultTimeout * 1000;
    }

    @GET
    @Timed
    public SynthesizedSound synthesize(@QueryParam("sentence") String sentence) {
        // todo add an optional parameter, ttl for the paragraph
        String message = "Received: <" + sentence + ">";
        LOGGER.info(message);

        String key = "synthesize-" + Thread.currentThread().getId();
        SpeechUnit speechUnit = new SpeechUnit(key, sentence);

        long timeout = System.currentTimeMillis() + defaultTimeout;

        synthesizer.addSpeechUnit(speechUnit);

        while (!synthesizer.isSpeechUnitReady(key)) {
            if (System.currentTimeMillis() > timeout) {
                return new SynthesizedSound();
            }

            pause();
        }

        SynthesizedSound result = synthesizer.getSynthesizedSound(key);

        message = "Returned: <" + result + ">";
        LOGGER.info(message);

        return result;
    }

    private void pause() {
        try {
            Thread.sleep(idleTime);
        } catch (InterruptedException e) {
            LOGGER.warn(e.getMessage());
        }
    }
}
