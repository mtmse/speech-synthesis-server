package se.mtm.speech.synthesis.invalidate;

import org.junit.Test;
import se.mtm.speech.synthesis.status.InvalidateFilibusterResource;
import se.mtm.speech.synthesis.synyhesize.SpeechSynthesizer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvalidateFilibusterResourceTest {

    @Test
    public void invalidate_filibusters() {
        SpeechSynthesizer synthesizer = mock(SpeechSynthesizer.class);

        InvalidateFilibusterResource invalidateFilibuster = new InvalidateFilibusterResource(synthesizer);
        invalidateFilibuster.invalidate();

        verify(synthesizer).invalidate();
    }
}
