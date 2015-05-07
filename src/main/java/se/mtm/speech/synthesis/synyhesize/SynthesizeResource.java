package se.mtm.speech.synthesis.synyhesize;


import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/synthesize")
@Produces(MediaType.APPLICATION_JSON)
public class SynthesizeResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpeechSynthesizer.class);

    private SpeechSynthesizer synthesizer;

    public SynthesizeResource(SpeechSynthesizer synthesizer) {
        this.synthesizer = synthesizer;
    }

    @GET
    @Timed
    public Synthesized synthesize(@QueryParam("paragraph") String paragraph) {
        LOGGER.info("Received: <" + paragraph + ">");

        return synthesizer.synthesize(paragraph);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SynthesizeResource that = (SynthesizeResource) o;
        return Objects.equal(synthesizer, that.synthesizer);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(synthesizer);
    }

    @Override
    public String toString() {
        return "SynthesizeResource{" +
                "synthesizer=" + synthesizer +
                '}';
    }
}
