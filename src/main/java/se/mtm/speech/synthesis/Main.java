package se.mtm.speech.synthesis;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import se.mtm.speech.synthesis.infrastructure.Configuration;
import se.mtm.speech.synthesis.infrastructure.HealthCheck;
import se.mtm.speech.synthesis.synyhesize.SpeechSynthesizer;
import se.mtm.speech.synthesis.synyhesize.SynthesizeResource;

public class Main extends Application<Configuration> {
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
    }

    @Override
    public String getName() {
        return "Speech synthesis";
    }

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
    }
}
