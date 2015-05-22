package se.mtm.speech.synthesis;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import se.mtm.speech.synthesis.infrastructure.Configuration;
import se.mtm.speech.synthesis.infrastructure.HealthCheck;
import se.mtm.speech.synthesis.synyhesize.SpeechSynthesizer;
import se.mtm.speech.synthesis.synyhesize.SynthesizeResource;

public class Main extends Application<Configuration> {
    public static void main(String... args) throws Exception {
        new Main().run(args);
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        environment.healthChecks().register("HealthCheck", new HealthCheck());

        int capacity = configuration.getCapacity();
        int filibusters = configuration.getFilibusters();
        int idleTime = configuration.getIdleTime();
        SpeechSynthesizer speechSynthesizer = new SpeechSynthesizer(capacity, filibusters, idleTime, true);
        environment.lifecycle().manage(speechSynthesizer);

        long defaultTimeout = configuration.getTimeout();
        SynthesizeResource synthesizer = new SynthesizeResource(speechSynthesizer, defaultTimeout);
        environment.jersey().register(synthesizer);
    }

    @Override
    public String getName() {
        return "Speech synthesis";
    }
}
