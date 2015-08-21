package se.mtm.speech.synthesis.infrastructure.configuration;

public class FakeSynthesize {
    private final boolean fake;

    public FakeSynthesize(boolean fake) {
        this.fake = fake;
    }

    public boolean isFake() {
        return fake;
    }
}
