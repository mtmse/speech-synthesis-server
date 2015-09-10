package se.mtm.speech.synthesis.synthesize;

import java.util.Date;

public abstract class Synthesizer {
    private SpeechUnit speechUnit;
    private final Date created;
    protected long timeToDie;

    public Synthesizer() {
        created = new Date();
    }

    void setSpeechUnit(SpeechUnit speechUnit) {
        this.speechUnit = speechUnit;
    }

    String getSpeechUnitKey() {
        return speechUnit.getKey();
    }

    String getSpeechUnitText() {
        return speechUnit.getText();
    }

    boolean isTooOld() {
        return System.currentTimeMillis() > timeToDie;
    }

    void prepareToDie() {
        this.timeToDie = 0;
    }

    abstract boolean isHealthy();

    boolean unHealthy() {
        return !isHealthy();
    }

    abstract void kill();

    public abstract String getType();

    public Date getCreated() {
        return created;
    }

    public Date getEndOfLife() {
        return new Date(timeToDie);
    }
}
