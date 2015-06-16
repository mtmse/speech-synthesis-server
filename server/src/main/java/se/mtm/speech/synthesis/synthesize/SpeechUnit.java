package se.mtm.speech.synthesis.synthesize;

class SpeechUnit {
    private final String text;
    private String key;

    SpeechUnit(String text) {
        this.text = text;
    }

    SpeechUnit(String key, String text) {
        this.key = key;
        this.text = text;
    }

    String getKey() {
        return key;
    }

    String getText() {
        return text;
    }
}
