package se.mtm.speech.synthesis.synyhesize;

class Synthesizer {
    private SpeechUnit speechUnit;
    private long timeToDie;

    Synthesizer(long timeToDie) {
        this.timeToDie = timeToDie;
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

    void setTimeToDie(long timeToDie) {
        this.timeToDie = timeToDie;
    }
}
