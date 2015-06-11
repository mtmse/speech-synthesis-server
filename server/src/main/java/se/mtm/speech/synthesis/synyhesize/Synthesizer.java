package se.mtm.speech.synthesis.synyhesize;

public interface Synthesizer {
    void setSpeechUnit(SpeechUnit speechUnit);

    boolean isTooOld();

    void setTimeToDie(long timeToDie);
}
