package se.mtm.speech.synthesis;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import se.mtm.speech.synthesis.infrastructure.Configuration;
import se.mtm.speech.synthesis.infrastructure.FilibusterHealthCheck;
import se.mtm.speech.synthesis.status.InvalidateFilibusterResource;
import se.mtm.speech.synthesis.status.StatusResource;
import se.mtm.speech.synthesis.synthesize.SpeechSynthesizer;
import se.mtm.speech.synthesis.synthesize.SynthesizeResource;

public class Main extends Application<Configuration> {
    public static void main(String... args) throws Exception {
        new Main().run(args);
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        int capacity = configuration.getCapacity();
        int maxFilibusters = configuration.getMaxFilibusters();
        String filibusterHome = configuration.getFilibusterHome();
        String logHome = configuration.getLogHome();
        int minimumMemory = configuration.getMinimumMemory();
        int timeout = configuration.getTimeout();
        int timeToLive = configuration.getTimeToLive();
        int idleTime = configuration.getIdleTime();
        boolean fakeSynthesize = configuration.isFakeSynthesize();
        SpeechSynthesizer speechSynthesizer = new SpeechSynthesizer(capacity, maxFilibusters, minimumMemory, filibusterHome, logHome, timeout, timeToLive, idleTime, fakeSynthesize);
        environment.lifecycle().manage(speechSynthesizer);

        environment.healthChecks().register("Filibuster health check", new FilibusterHealthCheck(speechSynthesizer));

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
