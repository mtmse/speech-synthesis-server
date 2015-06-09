package se.mtm.speech.synthesis;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import se.mtm.speech.synthesis.infrastructure.Configuration;
import se.mtm.speech.synthesis.infrastructure.HealthCheck;
import se.mtm.speech.synthesis.status.InvalidateFilibusterResource;
import se.mtm.speech.synthesis.status.StatusResource;
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
        int maxFilibusters = configuration.getMaxFilibusters();
        int timeToLive = configuration.getTimeToLive();
        int idleTime = configuration.getIdleTime();
        SpeechSynthesizer speechSynthesizer = new SpeechSynthesizer(capacity, maxFilibusters, timeToLive, idleTime, true);
        environment.lifecycle().manage(speechSynthesizer);

        long defaultTimeout = configuration.getTimeout();
        SynthesizeResource synthesizer = new SynthesizeResource(speechSynthesizer, defaultTimeout, idleTime);
        environment.jersey().register(synthesizer);

        InvalidateFilibusterResource invalidator = new InvalidateFilibusterResource(speechSynthesizer);
        environment.jersey().register(invalidator);

        StatusResource status = new StatusResource();
        environment.jersey().register(status);
    }

    @Override
    public String getName() {
        return "Speech synthesis";
    }

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
        bootstrap.addBundle(new ViewBundle());
        bootstrap.addBundle(new AssetsBundle());
    }
}
