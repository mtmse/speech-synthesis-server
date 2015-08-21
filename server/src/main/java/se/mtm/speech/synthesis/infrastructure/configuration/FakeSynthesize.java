package se.mtm.speech.synthesis.infrastructure.configuration;

/**
 * fake the synthesize, probably for testing
 */
public class FakeSynthesize {
    private final boolean fake;

    public FakeSynthesize(boolean fake) {
        this.fake = fake;
    }

    public boolean isFake() {
        return fake;
    }
}
