package se.mtm.speech.synthesis.infrastructure.configuration;

import java.io.File;

/**
 * The log directory
 */
public class LogHome {
    private String home;

    public LogHome(String home) {
        this.home = home;
        if (!home.endsWith("/")) {
            this.home += "/";
        }
    }

    public String getHome() {
        return home;
    }

    public File getLogHome() {
        return new File(home);
    }

    @Override
    public String toString() {
        return home;
    }
}
