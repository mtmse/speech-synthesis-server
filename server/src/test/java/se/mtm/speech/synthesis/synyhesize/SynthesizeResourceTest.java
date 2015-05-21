package se.mtm.speech.synthesis.synyhesize;

import org.junit.Test;

import java.nio.charset.Charset;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SynthesizeResourceTest {
    @Test
    public void synthesize_a_sentence() { // NOPMD
        String sentence = "The brown fox jumped over the lazy dog";
        byte[] sound = sentence.getBytes(Charset.forName("UTF-8"));
        ParagraphReady expected = new ParagraphReady(sentence, sound);
        SpeechSynthesizer synthesizer = mock(SpeechSynthesizer.class);
        when(synthesizer.popParagraph(anyString())).thenReturn(expected);
        long timeout = 100;
        SynthesizeResource synthesizeRest = new SynthesizeResource(synthesizer, timeout);

        ParagraphReady actual = (ParagraphReady) synthesizeRest.synthesize(sentence);

        assertThat(actual, is(expected));
    }

    @Test
    public void synthesise_a_sentence_and_fail_with_a_timeout() throws Exception {
        String sentence = "The brown fox jumped over the lazy dog";

        SpeechSynthesizer synthesizer = mock(SpeechSynthesizer.class);
        when(synthesizer.isParagraphReady(anyString())).thenReturn(new ParagraphNotReady());

        SynthesizeResource synthesizeRest = new SynthesizeResource(synthesizer, 17);

        ParagraphInterface actual = synthesizeRest.synthesize(sentence);

        assertTrue("The synthesise should have timed out", actual instanceof ParagraphTimedout);
    }
}
