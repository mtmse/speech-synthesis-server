package se.mtm.speech.synthesis.status;

import se.mtm.speech.synthesis.infrastructure.SpeechSynthesisException;
import se.mtm.speech.synthesis.synthesize.SynthesizeResource;
import se.mtm.speech.synthesis.synthesize.SynthesizedSound;

import javax.ws.rs.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

@Path("/testSynthesize")
public class TestSynthesizeResource {
    private final SynthesizeResource synthesizer;

    public TestSynthesizeResource(SynthesizeResource synthesizer) {
        this.synthesizer = synthesizer;
    }

    @GET
    @Produces("text/html;charset=UTF-8")
    public TestSynthesizeView showTestSynthesizeForm() {
        return new TestSynthesizeView();
    }

    @POST // NOPMD
    @Produces("audio/wav")
    public byte[] testSynthesize(@FormParam("sampleText") String sampleText) throws IOException {
        String encoded;
        try {
            encoded = Base64.getEncoder().encodeToString(sampleText.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new SpeechSynthesisException(e.getMessage(), e);
        }

        SynthesizedSound sound = synthesizer.synthesize(encoded);
        return sound.getSound();
    }
}
