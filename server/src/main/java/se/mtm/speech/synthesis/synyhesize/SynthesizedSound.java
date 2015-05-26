package se.mtm.speech.synthesis.synyhesize;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.Objects;

public class SynthesizedSound {
    @JsonIgnore
    private String key;
    private byte[] sound;
    private boolean timeout;

    public SynthesizedSound() {
        this("", new byte[0], true);
    }

    public SynthesizedSound(String key, byte[] sound) {
        this(key, sound, false);
    }

    private SynthesizedSound(String key, byte[] sound, boolean timeout) {
        this.key = key;
        this.sound = Arrays.copyOf(sound, sound.length);
        this.timeout = timeout;
    }

    public String getKey() {
        return key;
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
        return key.equals(other.key) &&
                timeout == other.timeout &&
                Arrays.equals(sound, other.sound);
    }

    @Override
    public int hashCode() {
        String soundAsString = new String(sound);
        return Objects.hash(key, soundAsString, timeout);
    }

    @Override
    public String toString() {
        return "SynthesizedSound{" +
                "key=" + key +
                "sound=" + Arrays.toString(sound) +
                ", timeout=" + timeout +
                '}';
    }
}
