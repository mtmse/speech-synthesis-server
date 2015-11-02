package se.mtm.speech.synthesis.webui;

import org.junit.Test;
import se.mtm.speech.synthesis.synthesize.SynthesizeResource;
import se.mtm.speech.synthesis.synthesize.SynthesizedSound;

import java.util.Base64;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class TestSynthesizeResourceTest {
    @Test
    public void return_correct_view() throws Exception {
        SynthesizeResource synthesizer = mock(SynthesizeResource.class);
        TestSynthesizeResource resource = new TestSynthesizeResource(synthesizer);

        TestSynthesizeView actual = resource.showTestSynthesizeForm();

        assertNotNull("Expected a correct view", actual);
    }

    @Test
    public void wire_up_the_synthesis() throws Exception { // NOPMD
        SynthesizeResource synthesizer = mock(SynthesizeResource.class);
        SynthesizedSound sound = new SynthesizedSound.Builder()
                .build();
        when(synthesizer.synthesize(anyString())).thenReturn(sound);
        TestSynthesizeResource resource = new TestSynthesizeResource(synthesizer);

        String sampleText = "Not important content";

        String expected = Base64.getEncoder().encodeToString(sampleText.getBytes("UTF-8"));
        resource.testSynthesize(sampleText);

        verify(synthesizer, times(1)).synthesize(expected);
    }
}
