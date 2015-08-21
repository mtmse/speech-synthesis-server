package se.mtm.speech.synthesis.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.mtm.speech.synthesis.infrastructure.configuration.FilibusterHome;
import se.mtm.speech.synthesis.infrastructure.configuration.Timeout;

public class Configuration extends io.dropwizard.Configuration {

    @JsonProperty
    private int capacity = 17; // NOPMD set by reflection

    @JsonProperty
    private int timeout = 30; // NOPMD set by reflection

    @JsonProperty
    private int maxFilibusters = 6; // NOPMD set by reflection

    @JsonProperty
    private String filibusterHome = "undefined"; // NOPMD set by reflection

    @JsonProperty
    private String logHome = "undefined"; // NOPMD set by reflection

    @JsonProperty
    private int minimumMemory = 2; // NOPMD set by reflection

    @JsonProperty
    private int timeToLive = 25; // NOPMD set by reflection

    @JsonProperty
    private int idleTime = 100; // NOPMD set by reflection

    @JsonProperty
    private boolean fake = false; // NOPMD set by reflection

    public Timeout getTimeout() {
        return new Timeout(timeout);
    }

    public int getCapacity() {
        return capacity;
    }

    public int getMaxFilibusters() {
        return maxFilibusters;
    }

    public int getMinimumMemory() {
        return minimumMemory;
    }

    public int getIdleTime() {
        return idleTime;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public boolean isFakeSynthesize() {
        return fake;
    }

    public FilibusterHome getFilibusterHome() {
        return new FilibusterHome(filibusterHome);
    }

    public String getLogHome() {
        return logHome;
    }
}
