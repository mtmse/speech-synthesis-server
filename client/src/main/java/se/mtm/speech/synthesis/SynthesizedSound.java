package se.mtm.speech.synthesis;

import java.util.Arrays;

public class SynthesizedSound {
    private byte[] sound;
    private boolean timeout;

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
