package se.mtm.speech.synthesis;

import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class SpeechClientTest {
    @Test
    public void call_server() throws IOException { // NOPMD
        String expectedSentence = "Hello Filibuster!";

        Client httpClient = mock(Client.class);
        WebTarget webTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        String json = "{\"sentence\":\"Hello Filibuster!\",\"sound\":\"SGVsbG8gRmlsaWJ1c3RlciE=\"}";

        when(httpClient.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(webTarget);
        when(webTarget.queryParam(anyString(), anyString())).thenReturn(webTarget);
        when(webTarget.request(any(MediaType.class))).thenReturn(builder);
        when(builder.get(String.class)).thenReturn(json);

        String host = "localhost";
        int port = 80;
        SpeechClient client = new SpeechClient(httpClient, host, port);

        Paragraph actual = client.synthesise(expectedSentence);

        assertThat(actual.getSentence(), is(expectedSentence)); // NOPMD
        assertThat(actual.getSound(), is(expectedSentence.getBytes()));  // NOPMD

        verify(httpClient).target("http://localhost:80");
        verify(webTarget).path("synthesize");
        verify(webTarget).queryParam("sentence", expectedSentence);
        verify(webTarget).request(MediaType.APPLICATION_JSON_TYPE);
        verify(builder).get(String.class);
    }
}
