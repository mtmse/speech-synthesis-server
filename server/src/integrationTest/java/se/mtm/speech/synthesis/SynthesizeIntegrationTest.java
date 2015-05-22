package se.mtm.speech.synthesis;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.Rule;
import org.junit.Test;
import se.mtm.speech.synthesis.infrastructure.Configuration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SynthesizeIntegrationTest {
    @Rule
    public final DropwizardAppRule<Configuration> application =
            new DropwizardAppRule<>(Main.class, ResourceHelpers.resourceFilePath("test-configuration.yaml"));

    @Test
    public void synthesize_a_sentence() throws Exception { // NOPMD
        int port = application.getLocalPort();
        SpeechClient client = new SpeechClient("localhost", port);

        Paragraph actual = client.synthesise("Hello Filibuster!");

        assertThat(actual.getSentence(), is("Hello Filibuster!")); // NOPMD
    }
}
