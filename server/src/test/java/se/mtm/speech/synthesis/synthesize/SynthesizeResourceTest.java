package se.mtm.speech.synthesis.synthesize;

import org.junit.Test;
import se.mtm.speech.synthesis.infrastructure.configuration.IdleTime;
import se.mtm.speech.synthesis.infrastructure.configuration.Timeout;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Base64;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SynthesizeResourceTest {
    @Test
    public void synthesize_a_sentence() throws Exception {
        String sentence = "The brown fox jumped over the lazy dog";
        String encodedSentence = getEncodedString(sentence);
        byte[] sound = encodedSentence.getBytes(Charset.forName("UTF-8"));
        String key = "17";
        SynthesizedSound expected = new SynthesizedSound.Builder()
                .key(key)
                .sound(sound)
                .build();

        SpeechSynthesizer synthesizer = mock(SpeechSynthesizer.class);
        when(synthesizer.addSpeechUnit(any(SpeechUnit.class))).thenReturn(true);
        when(synthesizer.isSpeechUnitReady(anyString())).thenReturn(true);
        when(synthesizer.getSynthesizedSound(anyString())).thenReturn(expected);
        Timeout timeout = new Timeout(1);
        IdleTime idleTime = new IdleTime(90);
        SynthesizeResource synthesizeRest = new SynthesizeResource(synthesizer, timeout, idleTime);

        SynthesizedSound actual = synthesizeRest.synthesize(encodedSentence);

        assertThat(actual, is(expected));
    }

    @Test
    public void synthesise_a_sentence_and_fail_with_a_timeout() throws Exception {
        String sentence = "The brown fox jumped over the lazy dog";
        String encodedSentence = getEncodedString(sentence);

        SpeechSynthesizer synthesizer = mock(SpeechSynthesizer.class);
        when(synthesizer.addSpeechUnit(any(SpeechUnit.class))).thenReturn(true);
        when(synthesizer.isSpeechUnitReady(anyString())).thenReturn(false);

        Timeout defaultTimeout = new Timeout(0);
        IdleTime idleTime = new IdleTime(0);
        SynthesizeResource synthesizeRest = new SynthesizeResource(synthesizer, defaultTimeout, idleTime);

        SynthesizedSound actual = synthesizeRest.synthesize(encodedSentence);

        assertTrue("The synthesise should have timed out", actual.isTimeout());
    }

    @Test
    public void return_not_accepted_if_the_in_que_is_full() throws Exception {
        SpeechSynthesizer synthesizer = mock(SpeechSynthesizer.class);
        when(synthesizer.addSpeechUnit(any(SpeechUnit.class))).thenReturn(false);

        Timeout defaultTimeout = new Timeout(0);
        IdleTime idleTime = new IdleTime(0);
        SynthesizeResource synthesizeRest = new SynthesizeResource(synthesizer, defaultTimeout, idleTime);
        String sentence = "any sentence";
        String encodedSentence = getEncodedString(sentence);
        SynthesizedSound actual = synthesizeRest.synthesize(encodedSentence);

        assertTrue("The sentence should not have been accepted", actual.isNotAccepted());
    }

    private String getEncodedString(String clearText) throws UnsupportedEncodingException {
        return Base64.getEncoder().encodeToString(clearText.getBytes("UTF-8"));
    }
}
