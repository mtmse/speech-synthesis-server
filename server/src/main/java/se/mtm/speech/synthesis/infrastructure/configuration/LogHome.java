package se.mtm.speech.synthesis.infrastructure.configuration;

public class LogHome {
    private String home ;

    public LogHome(String home) {
        this.home = home;
        if (!home.endsWith("/")) {
            this.home += "/";
        }
    }

    public String getHome() {
        return home;
    }
}
