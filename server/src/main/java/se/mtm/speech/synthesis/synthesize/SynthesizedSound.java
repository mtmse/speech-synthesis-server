package se.mtm.speech.synthesis.synthesize;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.Objects;

public class SynthesizedSound {
    @JsonIgnore
    private final String key;
    private final byte[] sound;
    private final boolean timeout;
    private final boolean notAccepted;

    private SynthesizedSound(Builder builder) {
        this.key = builder.key;
        this.sound = builder.sound;
        this.timeout = builder.timeout;
        this.notAccepted = builder.notAccepted;
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

    public boolean isNotAccepted() {
        return notAccepted;
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
                notAccepted == other.notAccepted &&
                Arrays.equals(sound, other.sound);
    }

    @Override
    public int hashCode() {
        String soundAsString = new String(sound);
        return Objects.hash(key, soundAsString, timeout, notAccepted);
    }

    @Override
    public String toString() {
        return "SynthesizedSound{" +
                "key=" + key +
                "sound=" + Arrays.toString(sound) +
                ", timeout=" + timeout +
                ", notAccepted=" + notAccepted +
                '}';
    }

    public static class Builder {
        private String key = ""; // NOPMD
        private byte[] sound = new byte[0]; // NOPMD
        private boolean timeout = false; // NOPMD
        private boolean notAccepted = false; // NOPMD

        public Builder() {
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder sound(byte[] sound) {
            this.sound = Arrays.copyOf(sound, sound.length);
            return this;
        }

        public Builder timeout() {
            timeout = true;
            return this;
        }

        public Builder notAccepted() {
            notAccepted = true;
            return this;
        }

        public SynthesizedSound build() {
            return new SynthesizedSound(this); // NOPMD
        }
    }
}
