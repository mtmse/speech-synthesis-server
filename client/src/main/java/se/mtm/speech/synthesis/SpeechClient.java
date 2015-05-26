package se.mtm.speech.synthesis;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

public class SpeechClient {
    private final Client client;
    private final String host;
    private final int port;
    private final ObjectMapper mapper = new ObjectMapper();

    public SpeechClient(String host, int port) {
        this(ClientBuilder.newClient(), host, port);
    }

    public SpeechClient(Client client, String host, int port) {
        this.client = client;
        this.host = host;
        this.port = port;
    }

    public SynthesizedSound synthesise(String sentence) {
        String json = client.target("http://" + host + ":" + port)
                .path("synthesize")
                .queryParam("sentence", sentence)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(String.class);

        try {
            return mapper.readValue(json, SynthesizedSound.class);
        } catch (IOException e) {
            throw new SpeechSynthesisClientException(e.getMessage(), e);
        }
    }
}
