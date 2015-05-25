package se.mtm.speech.synthesis;

import java.util.Arrays;

public class Paragraph {
    private String sentence;
    private byte[] sound;

    public String getSentence() {
        return sentence;
    }

    public byte[] getSound() {
        return Arrays.copyOf(sound, sound.length);
    }

    @Override
    public String toString() {
        return "se.mtm.speech.synthesis.Paragraph{" +
                "sentence='" + sentence + '\'' +
                ", sound=" + Arrays.toString(sound) +
                '}';
    }
}
