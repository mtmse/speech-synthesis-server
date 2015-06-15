package se.mtm.speech.synthesis.invalidate;

import org.junit.Test;
import se.mtm.speech.synthesis.status.InvalidateFilibusterResource;
import se.mtm.speech.synthesis.synthesize.SpeechSynthesizer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvalidateFilibusterResourceTest {

    @Test
    public void invalidate_filibusters() { // NOPMD
        SpeechSynthesizer synthesizer = mock(SpeechSynthesizer.class);

        InvalidateFilibusterResource resource = new InvalidateFilibusterResource(synthesizer);
        resource.invalidate();

        verify(synthesizer).invalidate();
    }
}
