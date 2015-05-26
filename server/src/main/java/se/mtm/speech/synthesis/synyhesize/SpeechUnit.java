package se.mtm.speech.synthesis.synyhesize;

import java.util.Objects;

public class SpeechUnit {
    private final String text;

    public SpeechUnit(String text) {
        this.text = text;
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
        return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }

    @Override
    public String toString() {
        return "SpeechUnit{" +
                "text='" + text + '\'' +
                '}';
    }
}
