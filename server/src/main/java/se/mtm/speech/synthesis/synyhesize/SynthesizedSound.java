package se.mtm.speech.synthesis.synyhesize;

import java.util.Arrays;
import java.util.Objects;

public class SynthesizedSound {
    private byte[] sound;
    private boolean timeout;

    public SynthesizedSound() {
        this(new byte[0], true);
    }

    public SynthesizedSound(byte[] sound) {
        this(sound, false);
    }

    private SynthesizedSound(byte[] sound, boolean timeout) {
        this.sound = Arrays.copyOf(sound, sound.length);
        this.timeout = timeout;
    }

    public byte[] getSound() {
        return Arrays.copyOf(sound, sound.length);
    }

    public boolean isTimeout() {
        return timeout;
    }

    @Override
    public boolean equals(Object rhs) {
        if (this == rhs) {
            return true;
        }
        if (rhs == null || getClass() != rhs.getClass()) {
            return false;
        }
        SynthesizedSound other = (SynthesizedSound) rhs;
        return timeout == other.timeout &&
                Arrays.equals(sound, other.sound);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sound, timeout);
    }

    @Override
    public String toString() {
        return "SynthesizedSound{" +
                "sound=" + Arrays.toString(sound) +
                ", timeout=" + timeout +
                '}';
    }
}
