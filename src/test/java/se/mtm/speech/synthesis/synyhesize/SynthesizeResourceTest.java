package se.mtm.speech.synthesis.synyhesize;

import org.junit.Test;

import java.nio.charset.Charset;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SynthesizeResourceTest {
    @Test
    public void synthesize_a_sentence() {
        String paragraph = "The brown fox jumped over the lazy dog";
        byte[] sound = paragraph.getBytes(Charset.forName("UTF-8"));
        String key = "17";
        Paragraph expected = new Paragraph(key, paragraph, sound);
        SpeechSynthesizer synthesizer = mock(SpeechSynthesizer.class);
        when(synthesizer.getParagraph(key)).thenReturn(expected);
        SynthesizeResource synthesizeResource = new SynthesizeResource(synthesizer);

        Paragraph actual = synthesizeResource.synthesize(paragraph);

        assertThat(actual, is(expected));
    }
}
