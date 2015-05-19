package se.mtm.speech.synthesis.synyhesize;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ParagraphTest {
    private String key = "42";
    private String sentence = "A brown fox";

    @Test
    public void set_key_and_sentence() {
        Paragraph paragraph = new Paragraph(key, sentence);

        assertThat(paragraph.getKey(), is(key));
        assertThat(paragraph.getSentence(), is(sentence));
    }

    @Test
    public void set_key_sentence_and_sound() {
        byte[] sound = sentence.getBytes();

        Paragraph paragraph = new Paragraph(key, sentence, sound);

        assertThat(paragraph.getKey(), is(key));
        assertThat(paragraph.getSentence(), is(sentence));
        assertThat(paragraph.getSound(), is(sound));
    }

    @Test
    public void equal_object_are_equal() {
        Paragraph first = new Paragraph(key, sentence);
        Paragraph second = new Paragraph(key, sentence);

        assertTrue("Similar object should be equal", first.equals(second));
    }

    @Test
    public void same_hash_for_equal_paragraphs() {
        Paragraph first = new Paragraph(key, sentence);
        Paragraph second = new Paragraph(key, sentence);

        assertThat(first.hashCode(), is(second.hashCode()));
    }
}
