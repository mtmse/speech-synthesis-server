package se.mtm.speech.synthesis.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.mtm.speech.synthesis.infrastructure.configuration.*;

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

    public Capacity getCapacity() {
        return new Capacity(capacity);
    }

    public MaxFilibusters getMaxFilibusters() {
        return new MaxFilibusters(maxFilibusters);
    }

    public MinimumMemory getMinimumMemory() {
        return new MinimumMemory(minimumMemory);
    }

    public IdleTime getIdleTime() {
        return new IdleTime(idleTime);
    }

    public TimeToLive getTimeToLive() {
        return new TimeToLive(timeToLive);
    }

    public FakeSynthesize getFakeSynthesize() {
        return new FakeSynthesize(fake);
    }

    public FilibusterHome getFilibusterHome() {
        return new FilibusterHome(filibusterHome);
    }

    public LogHome getLogHome() {
        return new LogHome(logHome);
    }
}
