package se.mtm.speech.synthesis.synyhesize;

import org.junit.Test;

import java.nio.charset.Charset;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SynthesizeResourceTest {
    @Test
    public void synthesize_a_sentence() { // NOPMD
        String paragraph = "The brown fox jumped over the lazy dog";
        byte[] sound = paragraph.getBytes(Charset.forName("UTF-8"));
        Paragraph expected = new Paragraph(paragraph, sound);
        SpeechSynthesizer synthesizer = mock(SpeechSynthesizer.class);
        when(synthesizer.getParagraph(anyString())).thenReturn(expected);
        SynthesizeResource synthesizeRest = new SynthesizeResource(synthesizer);

        Paragraph actual = synthesizeRest.synthesize(paragraph);

        assertThat(actual, is(expected));
    }
}
