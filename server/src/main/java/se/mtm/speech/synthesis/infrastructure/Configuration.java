package se.mtm.speech.synthesis.infrastructure;

public class Configuration extends io.dropwizard.Configuration {
    public int getCapacity() {
        // todo get from the config file
        return 17;
    }
}
