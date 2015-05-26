package se.mtm.speech.synthesis.synyhesize;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.Transient;
import java.util.Arrays;

@Deprecated
public class ParagraphReady implements Paragraph {
    @JsonIgnore
    private String key;

    @JsonIgnore
    private final long timeToLive = -1;  // todo should be possible to set from the client, how can I get that to work?

    private String sentence;
    private byte[] sound;

    public ParagraphReady() {
    }

    public ParagraphReady(String key, String sentence) {
        this.key = key;
        this.sentence = sentence;
    }

    public ParagraphReady(String sentence, byte... sound) {
        this.sentence = sentence;
        this.sound =  Arrays.copyOf(sound, sound.length);
    }

    public ParagraphReady(String key, String sentence, byte[] sound) {
        this.key = key;
        this.sentence = sentence;
        this.sound =  Arrays.copyOf(sound, sound.length);
    }

    @Transient
    public String getKey() {
        return key;
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    @JsonProperty
    public String getSentence() {
        return sentence;
    }

    @JsonProperty
    public byte[] getSound() {
        if (sound == null) {
            return new byte[0];
        }

        return Arrays.copyOf(sound, sound.length);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {return false;}
        ParagraphReady paragraphReady = (ParagraphReady) other;
        return java.util.Objects.equals(key, paragraphReady.key) &&
                java.util.Objects.equals(sentence, paragraphReady.sentence);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(key, sentence);
    }

    @Override
    public String toString() {
        return "ParagraphReady{" +
                "key='" + key + '\'' +
                ", sentence='" + sentence + '\'' +
                '}';
    }
}
