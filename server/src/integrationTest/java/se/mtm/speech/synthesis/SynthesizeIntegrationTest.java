package se.mtm.speech.synthesis;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SynthesizeIntegrationTest {
    private static int port;

    @BeforeClass // NOPMD
    public static void setUp() throws Exception {
        Main application = ApplicationRunner.runApplication();
        port = application.getHttpPort();
    }

    @Test
    public void synthesize3() throws Exception {
        SpeechClient client = new SpeechClient("localhost", port);

        Paragraph actual = client.synthesise("Hello Filibuster!");

        assertThat(actual.getSentence(), is("Hello Filibuster!"));
    }
}
