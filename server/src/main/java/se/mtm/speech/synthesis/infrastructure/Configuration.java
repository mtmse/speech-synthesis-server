package se.mtm.speech.synthesis.infrastructure;

public class Configuration extends io.dropwizard.Configuration {

    public int getTimeout() {
        // todo get from the config file
        return 30;
    }

    public int getCapacity() {
        // todo get from the config file
        return 17;
    }

    public int getFilibusters() {
        // todo get from the config file
        return 10;
    }

    public int getIdleTime() {
        // todo get from the config file
        return 100;
    }
}
