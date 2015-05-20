package se.mtm.speech.synthesis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public final class ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationRunner.class);

    private ApplicationRunner() {
    }

    public static Main runApplication() {
        URL configResource = ApplicationRunner.class.getResource("/test-configuration.yaml");
        String configFile = configResource.getFile();

        String mode = "server";
        Main application = new Main();
        String[] args = {mode, configFile};
        try {
            application.run(args);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            System.exit(1);
        }

        return application;
    }
}
