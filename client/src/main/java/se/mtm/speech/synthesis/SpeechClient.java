package se.mtm.speech.synthesis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.client.ClientProperties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SpeechClient {
    private final Client client;
    private final String host;
    private final int port;
    private final ObjectMapper mapper;

    public SpeechClient(String host, int port) {
        this(ClientBuilder.newClient(), new ObjectMapper(), host, port);
    }

    SpeechClient(Client client, ObjectMapper mapper, String host, int port) {
        this.client = client;
        this.mapper = mapper;
        this.host = host;
        this.port = port;

        int fifteenMinutes = 1000 * 60 * 15;
        client.property(ClientProperties.READ_TIMEOUT, fifteenMinutes);
    }

    public SynthesizedSound synthesise(String sentence) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(sentence, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new SpeechSynthesisClientException(e.getMessage(), e);
        }

        String json = client.target("http://" + host + ":" + port)
                .path("synthesize")
                .queryParam("sentence", encoded)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(String.class);

        try {
            return mapper.readValue(json, SynthesizedSound.class);
        } catch (IOException e) {
            System.err.println(e.getMessage()); // NOPMD PipeOnline monitors std err
            throw new SpeechSynthesisClientException(e.getMessage(), e);
        }
    }
}
