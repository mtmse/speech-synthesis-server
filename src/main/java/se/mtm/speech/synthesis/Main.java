package se.mtm.speech.synthesis;

import io.dropwizard.Application;
import io.dropwizard.jetty.ConnectorFactory;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import se.mtm.speech.synthesis.infrastructure.Configuration;
import se.mtm.speech.synthesis.infrastructure.HealthCheck;
import se.mtm.speech.synthesis.synyhesize.SpeechSynthesizer;
import se.mtm.speech.synthesis.synyhesize.SynthesizeResource;

public class Main extends Application<Configuration> {
    private int httpPort;

    public static void main(String[] args) throws Exception {
        new Main().run(args);
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        environment.healthChecks().register("HealthCheck", new HealthCheck());

        SpeechSynthesizer speechSynthesizer = new SpeechSynthesizer();
        environment.lifecycle().manage(speechSynthesizer);

        SynthesizeResource synthesizeResource = new SynthesizeResource(speechSynthesizer);
        environment.jersey().register(synthesizeResource);

        findHttpPort(configuration);
    }

    private void findHttpPort(Configuration configuration) {
        DefaultServerFactory serverFactory = (DefaultServerFactory) configuration.getServerFactory();
        for (ConnectorFactory connector : serverFactory.getApplicationConnectors()) {
            if (connector.getClass().isAssignableFrom(HttpConnectorFactory.class)) {
                httpPort = ((HttpConnectorFactory) connector).getPort();
            }
        }
    }

    @Override
    public String getName() {
        return "Speech synthesis";
    }

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
    }

    public int getListeningPort() {
        return httpPort;
    }
}
