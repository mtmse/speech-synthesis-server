package se.mtm.speech.synthesis.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Configuration extends io.dropwizard.Configuration {

    @JsonProperty
    private int capacity = 17; // NOPMD set by reflection

    @JsonProperty
    private int timeout = 30; // NOPMD set by reflection

    @JsonProperty
    private int filibusters = 6; // NOPMD set by reflection

    @JsonProperty
    private int idleTime = 100; // NOPMD set by reflection

    public int getTimeout() {
        return timeout;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getFilibusters() {
        return filibusters;
    }

    public int getIdleTime() {
        return idleTime;
    }
}
