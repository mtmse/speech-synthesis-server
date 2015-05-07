package se.mtm.speech.synthesis.synyhesize;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.nio.charset.Charset;
import java.util.Arrays;

public class Synthesized {
    private String paragraph;
    private byte[] sound;

    public Synthesized() {
    }

    public Synthesized(String paragraph, byte[] sound) {
        this.paragraph = paragraph;
        this.sound = sound;
    }

    @JsonProperty
    public String getParagraph() {
        return paragraph;
    }

    @JsonProperty
    public byte[] getSound() {
        return sound;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Synthesized that = (Synthesized) o;
        return Objects.equal(paragraph, that.paragraph);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(paragraph);
    }

    @Override
    public String toString() {
        return "Synthesized{" +
                "paragraph='" + paragraph + '\'' +
                ", sound=" + Arrays.toString(sound) +
                '}';
    }
}