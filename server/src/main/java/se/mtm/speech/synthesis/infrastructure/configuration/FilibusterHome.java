package se.mtm.speech.synthesis.infrastructure.configuration;

public class FilibusterHome {
    private String home;

    public FilibusterHome(String home) {
        this.home = home;
        if (!home.endsWith("/")) {
            this.home += "/";
        }
    }

    public String getHome() {
        return home;
    }
}
