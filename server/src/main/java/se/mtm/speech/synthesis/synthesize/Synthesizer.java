package se.mtm.speech.synthesis.synthesize;

abstract class Synthesizer {
    private SpeechUnit speechUnit;
    protected long timeToDie;

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
}
