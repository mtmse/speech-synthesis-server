package se.mtm.speech.synthesis.synthesize;

class SpeechUnit {
    private final String text;
    private final String key;

    SpeechUnit(String text) {
        this.key = "synthesize-" + Thread.currentThread().getId();
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
