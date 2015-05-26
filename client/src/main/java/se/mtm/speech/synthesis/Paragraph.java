package se.mtm.speech.synthesis;

import java.util.Arrays;

public class Paragraph {
    private String sentence;
    private byte[] sound;
    private boolean timeout;

    @Deprecated
    public String getSentence() {
        return sentence;
    }

    public byte[] getSound() {
        return Arrays.copyOf(sound, sound.length);
    }

    public boolean isTimeout() {
        return timeout;
    }

    @Override
    public String toString() {
        return "Paragraph{" +
                "sound=" + Arrays.toString(sound) +
                ", timeout=" + timeout +
                '}';
    }
}
