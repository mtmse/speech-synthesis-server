package se.mtm.speech.synthesis.invalidate;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.apache.http.HttpStatus;
import org.junit.Rule;
import org.junit.Test;
import se.mtm.speech.synthesis.Main;
import se.mtm.speech.synthesis.infrastructure.Configuration;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class InvalidateFilibusterIntegrationTest {
    @Rule
    public final DropwizardAppRule<Configuration> application =
            new DropwizardAppRule<>(Main.class, ResourceHelpers.resourceFilePath("test-configuration.yaml"));

    @Test
    public void invalidate_all_filibusters() throws Exception {
        int expected = HttpStatus.SC_OK;

        int port = application.getLocalPort();
        String host = "localhost";

        Client client = ClientBuilder.newClient();
        Invocation get = client.target("http://" + host + ":" + port)
                .path("invalidate")
                .request(MediaType.TEXT_HTML)
                .buildGet();

        Response response = get.invoke();
        int actual = response.getStatus();

        assertThat(actual, is(expected));
    }
}
