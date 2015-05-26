package se.mtm.speech.synthesis.synyhesize;

import java.util.Objects;

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

    @Override
    public boolean equals(Object rhs) {
        if (this == rhs) {
            return true;
        }
        if (rhs == null || getClass() != rhs.getClass()) {
            return false;
        }
        SpeechUnit that = (SpeechUnit) rhs;
        return Objects.equals(key, that.key) &&
                Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, text);
    }

    @Override
    public String toString() {
        return "SpeechUnit{" +
                "key='" + key + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}