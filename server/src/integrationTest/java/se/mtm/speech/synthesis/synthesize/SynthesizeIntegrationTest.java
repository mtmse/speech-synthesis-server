package se.mtm.speech.synthesis.synthesize;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.Rule;
import org.junit.Test;
import se.mtm.speech.synthesis.Main;
import se.mtm.speech.synthesis.SpeechClient;
import se.mtm.speech.synthesis.SynthesizedSound;
import se.mtm.speech.synthesis.infrastructure.Configuration;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SynthesizeIntegrationTest {
    @Rule
    public final DropwizardAppRule<Configuration> application =
            new DropwizardAppRule<>(Main.class, ResourceHelpers.resourceFilePath("test-configuration.yaml"));

    @Test
    public void synthesize_a_sentence() throws Exception {
        int port = application.getLocalPort();
        SpeechClient client = new SpeechClient("localhost", port);

        String sentence = "Hello Filibuster!";
        byte[] expectedSound = sentence.getBytes();

        SynthesizedSound actual = client.synthesise(sentence);

        assertFalse("The call should not have timed out", actual.isTimeout());
        assertThat(actual.getSound(), is(expectedSound));
    }

    @Test
    public void synthesise_complicated_sentence() throws Exception {
        int port = application.getLocalPort();
        SpeechClient client = new SpeechClient("localhost", port);

        String sentence = "tjänsteproduktionen (Hirschman 2008 {1970], s. 98–101).";
        byte[] expectedSound = sentence.getBytes();

        SynthesizedSound actual = client.synthesise(sentence);

        assertFalse("The call should not have timed out", actual.isTimeout());
        assertThat(actual.getSound(), is(expectedSound));
    }
}
