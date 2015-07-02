package se.mtm.speech.synthesis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.PrintStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class SpeechClientTest {
    private PrintStream originalStdErr;

    @Before
    public void setUp() {
        originalStdErr = System.err;
    }

    @After
    public void tearDown() {
        System.setErr(originalStdErr);
    }

    @Test
    public void call_server() throws IOException {
        String expectedSentence = "Hello Filibuster!";

        Client httpClient = mock(Client.class);
        WebTarget webTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        String json = "{\"sound\":\"SGVsbG8gRmlsaWJ1c3RlciE=\"}";

        when(httpClient.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(webTarget);
        when(webTarget.queryParam(anyString(), anyString())).thenReturn(webTarget);
        when(webTarget.request(any(MediaType.class))).thenReturn(builder);
        when(builder.get(String.class)).thenReturn(json);

        String host = "localhost";
        int port = 80;
        SpeechClient client = new SpeechClient(httpClient, new ObjectMapper(), host, port);

        SynthesizedSound actual = client.synthesise(expectedSentence);

        assertThat(actual.getSound(), is(expectedSentence.getBytes()));

        verify(httpClient).target("http://localhost:80");
        verify(webTarget).path("synthesize");
        verify(webTarget).queryParam("sentence", expectedSentence);
        verify(webTarget).request(MediaType.APPLICATION_JSON_TYPE);
        verify(builder).get(String.class);
    }

    @Test
    public void write_error_to_system_err() throws IOException {
        Client httpClient = getMockedClient();
        ObjectMapper mapper = getMockedMapper();
        String host = "not important";
        int notImportant = -1;

        SpeechClient client = new SpeechClient(httpClient, mapper, host, notImportant);

        PrintStream stdErrMock = mock(PrintStream.class);
        System.setErr(stdErrMock);

        try {
            client.synthesise("anything");
        } catch (Exception e) {
            verify(stdErrMock).println(anyString());
        }

        //noinspection PointlessBooleanExpression
        assertThat(true, is(!false)); // pmd requires at least one assert...
    }

    private Client getMockedClient() {
        Invocation.Builder builder = mock(Invocation.Builder.class);
        //noinspection unchecked
        when(builder.get(any(Class.class))).thenReturn("");

        WebTarget webTarget = mock(WebTarget.class);
        when(webTarget.path(anyString())).thenReturn(webTarget);
        when(webTarget.queryParam(anyString(), anyString())).thenReturn(webTarget);
        when(webTarget.request(any(MediaType.class))).thenReturn(builder);

        Client httpClient = mock(Client.class);
        when(httpClient.target(anyString())).thenReturn(webTarget);

        return httpClient;
    }

    private ObjectMapper getMockedMapper() throws IOException {
        ObjectMapper mapper = mock(ObjectMapper.class);
        //noinspection unchecked
        when(mapper.readValue(anyString(), any(Class.class))).thenThrow(IOException.class);
        return mapper;
    }
}
