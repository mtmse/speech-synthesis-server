package se.mtm.speech.synthesis;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.mtm.speech.synthesis.infrastructure.Configuration;
import se.mtm.speech.synthesis.infrastructure.FilibusterHealthCheck;
import se.mtm.speech.synthesis.infrastructure.configuration.*;
import se.mtm.speech.synthesis.status.*;
import se.mtm.speech.synthesis.synthesize.SpeechSynthesizer;
import se.mtm.speech.synthesis.synthesize.SynthesizeResource;
import se.mtm.speech.synthesis.version.VersionResource;

public class Main extends Application<Configuration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String... args) throws Exception {
        try {
            new Main().run(args);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e.getStackTrace());
        }
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        Capacity capacity = configuration.getCapacity();
        MaxFilibusters maxFilibusters = configuration.getMaxFilibusters();

        FilibusterHome filibusterHome = configuration.getFilibusterHome();

        LogHome logHome = configuration.getLogHome();

        MinimumMemory minimumMemory = configuration.getMinimumMemory();
        Timeout timeout = configuration.getTimeout();
        TimeToLive timeToLive = configuration.getTimeToLive();
        IdleTime idleTime = configuration.getIdleTime();
        FakeSynthesize fake = configuration.getFakeSynthesize();
        SpeechSynthesizer speechSynthesizer = new SpeechSynthesizer(capacity, maxFilibusters, minimumMemory, filibusterHome, logHome, timeout, timeToLive, idleTime, fake);
        environment.lifecycle().manage(speechSynthesizer);

        environment.healthChecks().register("Filibuster health check", new FilibusterHealthCheck(speechSynthesizer));

        Timeout defaultTimeout = configuration.getTimeout();
        SynthesizeResource synthesizer = new SynthesizeResource(speechSynthesizer, defaultTimeout, idleTime);
        environment.jersey().register(synthesizer);

        InvalidateFilibusterResource invalidator = new InvalidateFilibusterResource(speechSynthesizer);
        environment.jersey().register(invalidator);

        StatusResource status = new StatusResource(speechSynthesizer);
        environment.jersey().register(status);

        TestSynthesizeResource test = new TestSynthesizeResource(synthesizer);
        environment.jersey().register(test);

        LogsResource logs = new LogsResource(logHome);
        environment.jersey().register(logs);

        ShowLogResource showLog = new ShowLogResource(logHome);
        environment.jersey().register(showLog);

        AboutResource about = new AboutResource();
        environment.jersey().register(about);

        VersionResource version = new VersionResource();
        environment.jersey().register(version);
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
