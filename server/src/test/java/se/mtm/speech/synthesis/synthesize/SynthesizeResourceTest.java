package se.mtm.speech.synthesis.synthesize;

import org.junit.Test;

import java.nio.charset.Charset;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SynthesizeResourceTest {
    @Test
    public void synthesize_a_sentence() {
        String sentence = "The brown fox jumped over the lazy dog";
        byte[] sound = sentence.getBytes(Charset.forName("UTF-8"));
        String key = "17";
        SynthesizedSound expected = new SynthesizedSound.Builder()
                .key(key)
                .sound(sound)
                .build();

        SpeechSynthesizer synthesizer = mock(SpeechSynthesizer.class);
        when(synthesizer.addSpeechUnit(any(SpeechUnit.class))).thenReturn(true);
        when(synthesizer.isSpeechUnitReady(anyString())).thenReturn(true);
        when(synthesizer.getSynthesizedSound(anyString())).thenReturn(expected);
        long timeout = 100;
        long idleTime = 90;
        SynthesizeResource synthesizeRest = new SynthesizeResource(synthesizer, timeout, idleTime);

        SynthesizedSound actual = synthesizeRest.synthesize(sentence);

        assertThat(actual, is(expected));
    }

    @Test
    public void synthesise_a_sentence_and_fail_with_a_timeout() throws Exception {
        String sentence = "The brown fox jumped over the lazy dog";

        SpeechSynthesizer synthesizer = mock(SpeechSynthesizer.class);
        when(synthesizer.addSpeechUnit(any(SpeechUnit.class))).thenReturn(true);
        when(synthesizer.isSpeechUnitReady(anyString())).thenReturn(false);

        SynthesizeResource synthesizeRest = new SynthesizeResource(synthesizer, 0, 0);

        SynthesizedSound actual = synthesizeRest.synthesize(sentence);

        assertTrue("The synthesise should have timed out", actual.isTimeout());
    }

    @Test
    public void return_not_accepted_if_the_in_que_is_full() {
        SpeechSynthesizer synthesizer = mock(SpeechSynthesizer.class);
        when(synthesizer.addSpeechUnit(any(SpeechUnit.class))).thenReturn(false);

        SynthesizeResource synthesizeRest = new SynthesizeResource(synthesizer, 0, 0);
        SynthesizedSound actual = synthesizeRest.synthesize("any sentence");

        assertTrue("The sentence should not have been accepted", actual.isNotAccepted());
    }
}
