package se.mtm.speech.synthesis.synthesize;


import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.mtm.speech.synthesis.infrastructure.SpeechSynthesisException;
import se.mtm.speech.synthesis.infrastructure.configuration.IdleTime;
import se.mtm.speech.synthesis.infrastructure.configuration.Timeout;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Path("/synthesize")
@Produces(MediaType.APPLICATION_JSON)
public class SynthesizeResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(SynthesizeResource.class);

    private final SpeechSynthesizer synthesizer;
    private final IdleTime idleTime;
    private final Timeout defaultTimeout;

    public SynthesizeResource(SpeechSynthesizer synthesizer, Timeout defaultTimeout, IdleTime idleTime) {
        this.synthesizer = synthesizer;
        this.idleTime = idleTime;
        this.defaultTimeout = defaultTimeout ;
    }

    @GET
    @Timed
    public SynthesizedSound synthesize(@QueryParam("sentence") String sentence) {
        // todo add an optional parameter, timeout for the synthesis
        String message = "Received: <" + sentence + ">";
        LOGGER.info(message);

        String decoded;
        try {
            decoded = URLDecoder.decode(sentence, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new SpeechSynthesisException(e.getMessage(), e);
        }
        message = "Decoded to <" + decoded + ">";
        LOGGER.info(message);

        long start = System.currentTimeMillis();

        String key = "synthesize-" + Thread.currentThread().getId();
        SpeechUnit speechUnit = new SpeechUnit(key, decoded);

        long timeout = System.currentTimeMillis() + defaultTimeout.getTimeoutMilliseconds();

        if (!synthesizer.addSpeechUnit(speechUnit)) {
            return new SynthesizedSound.Builder()
                    .notAccepted()
                    .build();
        }

        while (!synthesizer.isSpeechUnitReady(key)) {
            if (System.currentTimeMillis() > timeout) {
                return new SynthesizedSound.Builder()
                        .timeout()
                        .build();
            }

            pause();
        }

        SynthesizedSound result = synthesizer.getSynthesizedSound(key);

        long stop = System.currentTimeMillis();

        message = "Returned the synthesised sound for <" + sentence + "> It took " + (stop - start) + "ms";
        LOGGER.info(message);

        return result;
    }

    private void pause() {
        try {
            Thread.sleep(idleTime.getIdle());
        } catch (InterruptedException e) {
            LOGGER.warn(e.getMessage());
        }
    }
}
