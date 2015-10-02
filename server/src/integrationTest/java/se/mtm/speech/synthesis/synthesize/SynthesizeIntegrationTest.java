package se.mtm.speech.synthesis.synthesize;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import se.mtm.speech.synthesis.Main;
import se.mtm.speech.synthesis.SpeechClient;
import se.mtm.speech.synthesis.SynthesizedSound;
import se.mtm.speech.synthesis.infrastructure.Configuration;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * We have had issues with character encoding. The characters that gave us problems are represented in
 * the parameters to this test. Add more examples if you experience problems with the character encoding.
 */
@RunWith(Parameterized.class)
public class SynthesizeIntegrationTest {
    private final String sentence;

    public SynthesizeIntegrationTest(String sentence) {
        this.sentence = sentence;
    }

    @Rule
    public final DropwizardAppRule<Configuration> application =
            new DropwizardAppRule<>(Main.class, ResourceHelpers.resourceFilePath("test-configuration.yaml"));

    @Test
    public void synthesize_a_sentence() throws Exception {
        int port = application.getLocalPort();
        SpeechClient client = new SpeechClient("localhost", port);

        byte[] expectedSound = sentence.getBytes();

        SynthesizedSound actual = client.synthesise(sentence);

        assertFalse("The call should not have timed out", actual.isTimeout());
        assertThat(actual.getSound(), is(expectedSound));
    }

    @Parameterized.Parameters // NOPMD
    public static Collection<String[]> testData() {
        List<String[]> expectedTestData = new LinkedList<>();

        expectedTestData.add(new String[]{"Hello Filibuster!"});
        expectedTestData.add(new String[]{"tjänsteproduktionen (Hirschman 2008 {1970], s. 98–101)."});
        expectedTestData.add(new String[]{"60 %, är = 17"});

        return expectedTestData;
    }
}
