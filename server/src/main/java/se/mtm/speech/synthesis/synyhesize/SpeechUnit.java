package se.mtm.speech.synthesis.synyhesize;

public class SpeechUnit {
    private final String key;
    private final String text;

    public SpeechUnit(String key, String text) {
        this.key = key;
        this.text = text;
    }

    public String getKey() {
        return key;
    }

    public String getText() {
        return text;
    }
}
