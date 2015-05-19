package se.mtm.speech.synthesis;

import org.junit.Before;
import org.junit.Test;
import se.mtm.speech.synthesis.synyhesize.Paragraph;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SynthesizeIntegrationTest {
    private int port;

    @Before
    public void setup() throws Exception {
        Main application = ApplicationRunner.runApplication();
        port = application.getHttpPort();
    }

    @Test
    public void synthesize() throws Exception {
        // Inspired from https:jersey.java.net/documentation/latest/client.html
        Client client = ClientBuilder.newClient();

        String expectedParagraph = "Hello Filibuster!";
        Paragraph entity = client.target("http://localhost:" + port)
                .path("synthesize")
                .queryParam("sentence", expectedParagraph)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(Paragraph.class);

        assertThat(entity.getSentence(), is("Hello Filibuster!"));
        assertThat(entity.getSound(), is("Hello Filibuster!".getBytes(Charset.forName("UTF-8"))));

        String json = client.target("http://localhost:" + port)
                .path("synthesize")
                .queryParam("paragraph", expectedParagraph)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(String.class);

        assertThat(json, is("{\"sentence\":\"Hello Filibuster!\",\"sound\":\"SGVsbG8gRmlsaWJ1c3RlciE=\"}"));
    }
}
