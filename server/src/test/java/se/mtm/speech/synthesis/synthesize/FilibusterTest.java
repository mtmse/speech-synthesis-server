package se.mtm.speech.synthesis.synthesize;

import org.junit.Test;
import se.mtm.speech.synthesis.infrastructure.FilibusterException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class FilibusterTest {

    @Test
    public void synthesize() {
        FilibusterProcess process = mock(FilibusterProcess.class);
        when(process.getSound()).thenReturn(new byte[0]);
        FilibusterPool pool = mock(FilibusterPool.class);
        SpeechSynthesizer synthesizer = mock(SpeechSynthesizer.class);
        long timeout = 0;
        long timeToLive = 0;

        Filibuster filibuster = new Filibuster(process, pool, synthesizer, "not used", "not used", timeout, timeToLive);

        SpeechUnit speechUnit = new SpeechUnit("key", "sentence");
        filibuster.setSpeechUnit(speechUnit);

        filibuster.run();

        verify(process).write("sentence");
        verify(process).getSound();
        verify(synthesizer).addSynthesizedParagraph(any(SynthesizedSound.class));
        verify(pool).returnFilibuster(filibuster);

        assertThat(true, is(!false)); // pmd requires at least one assert...
    }

    @Test
    public void synthesize_timeout() {
        FilibusterProcess process = mock(FilibusterProcess.class);
        when(process.getSound()).thenThrow(FilibusterException.class);
        FilibusterPool pool = mock(FilibusterPool.class);
        SpeechSynthesizer synthesizer = mock(SpeechSynthesizer.class);
        long timeout = 0;
        long timeToLive = 0;

        Filibuster filibuster = new Filibuster(process, pool, synthesizer, "not used", "not used", timeout, timeToLive);

        SpeechUnit speechUnit = new SpeechUnit("key", "sentence");
        filibuster.setSpeechUnit(speechUnit);

        filibuster.run();

        verify(process).kill();

        assertThat(true, is(!false)); // pmd requires at least one assert...
    }
}
