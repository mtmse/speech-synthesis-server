package se.mtm.speech.synthesis.status;

import se.mtm.speech.synthesis.synthesize.SynthesizeResource;
import se.mtm.speech.synthesis.synthesize.SynthesizedSound;

import javax.ws.rs.*;
import java.io.IOException;

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
        SynthesizedSound sound = synthesizer.synthesize(sampleText);
        return sound.getSound();
    }
}
