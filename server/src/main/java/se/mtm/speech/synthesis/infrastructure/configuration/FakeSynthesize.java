package se.mtm.speech.synthesis.infrastructure.configuration;

/**
 * Fake the synthesize, probably for testing
 */
public class FakeSynthesize {
    private final boolean fake;

    public FakeSynthesize() {
        fake = true;
    }

    public FakeSynthesize(boolean fake) {
        this.fake = fake;
    }

    public boolean isFake() {
        return fake;
    }
}
