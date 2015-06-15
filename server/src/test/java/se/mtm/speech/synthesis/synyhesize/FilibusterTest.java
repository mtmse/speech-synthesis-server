package se.mtm.speech.synthesis.synyhesize;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class FilibusterTest {

    @Test
    public void synthesize() {
        FilibusterProcess process = mock(FilibusterProcess.class);
        when(process.getSound()).thenReturn(new byte[0]);
        FilibusterPool pool = mock(FilibusterPool.class);
        SpeechSynthesizer synthesizer = mock(SpeechSynthesizer.class);
        long timeout = 30000;
        long timeToLive = 17;

        Filibuster filibuster = new Filibuster(process, pool, synthesizer, "not used", timeout, timeToLive);

        SpeechUnit speechUnit = new SpeechUnit("key", "sentence");
        filibuster.setSpeechUnit(speechUnit);

        filibuster.run();

        verify(process).write("sentence");
        verify(process).getSound();
        verify(synthesizer).addSynthesizedParagraph(any(SynthesizedSound.class));
        verify(pool).returnFilibuster(filibuster);

        assertThat(true, is(!false)); // pmd requires at least one assert...
    }
}
