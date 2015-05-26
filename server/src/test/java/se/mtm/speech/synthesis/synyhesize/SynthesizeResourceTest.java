package se.mtm.speech.synthesis.synyhesize;

import org.junit.Test;

import java.nio.charset.Charset;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SynthesizeResourceTest {
    @Test
    public void synthesize_a_sentence() {
        String sentence = "The brown fox jumped over the lazy dog";
        byte[] sound = sentence.getBytes(Charset.forName("UTF-8"));
        SynthesizedSound expected = new SynthesizedSound(sound);
        SpeechSynthesizer synthesizer = mock(SpeechSynthesizer.class);
        when(synthesizer.isSpeechUnitReady(anyString())).thenReturn(true);
        when(synthesizer.getSynthesizedSound(anyString())).thenReturn(new SynthesizedSound(sound));
        long timeout = 100;
        long idleTime = 90;
        SynthesizeResource synthesizeRest = new SynthesizeResource(synthesizer, timeout, idleTime);

        SynthesizedSound actual = synthesizeRest.synthesize(sentence);

        assertEquals(actual, expected);

        assertThat(actual, is(expected));
    }

    @Test
    public void synthesise_a_sentence_and_fail_with_a_timeout() throws Exception {
        String sentence = "The brown fox jumped over the lazy dog";

        SpeechSynthesizer synthesizer = mock(SpeechSynthesizer.class);
        when(synthesizer.isSpeechUnitReady(anyString())).thenReturn(false);

        SynthesizeResource synthesizeRest = new SynthesizeResource(synthesizer, 0, 0);

        SynthesizedSound actual = synthesizeRest.synthesize(sentence);

        assertTrue("The synthesise should have timed out", actual.isTimeout());
    }
}
