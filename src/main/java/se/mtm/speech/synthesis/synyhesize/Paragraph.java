package se.mtm.speech.synthesis.synyhesize;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.Transient;

public class Paragraph {
    @JsonIgnore
    private String key;

    private String sentence;
    private byte[] sound;

    public Paragraph() {
    }

    public Paragraph(String key, String sentence) {
        this.key = key;
        this.sentence = sentence;
    }

    public Paragraph(String key, String sentence, byte[] sound) {
        this.key = key;
        this.sentence = sentence;
        this.sound = sound;
    }

    @Transient
    public String getKey() {
        return key;
    }

    @JsonProperty
    public String getSentence() {
        return sentence;
    }

    @JsonProperty
    public byte[] getSound() {
        return sound;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paragraph paragraph = (Paragraph) o;
        return java.util.Objects.equals(key, paragraph.key) &&
                java.util.Objects.equals(sentence, paragraph.sentence);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(key, sentence);
    }

    @Override
    public String toString() {
        return "Paragraph{" +
                "key='" + key + '\'' +
                ", sentence='" + sentence + '\'' +
                '}';
    }
}
