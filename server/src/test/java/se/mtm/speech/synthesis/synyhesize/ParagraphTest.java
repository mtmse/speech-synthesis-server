package se.mtm.speech.synthesis.synyhesize;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ParagraphTest {
    private final static String KEY = "42";
    private final static String SENTENCE = "A brown fox";

    @Test
    public void set_key_and_sentence() { // NOPMD
        Paragraph paragraph = new Paragraph(KEY, SENTENCE);

        assertThat(paragraph.getKey(), is(KEY));
        assertThat(paragraph.getSentence(), is(SENTENCE));
    }

    @Test
    public void set_key_sentence_and_sound() { // NOPMD
        byte[] sound = SENTENCE.getBytes();

        Paragraph paragraph = new Paragraph(KEY, SENTENCE, sound);

        assertThat(paragraph.getKey(), is(KEY));
        assertThat(paragraph.getSentence(), is(SENTENCE));
        assertThat(paragraph.getSound(), is(sound));
    }

    @Test
    public void equal_object_are_equal() { // NOPMD
        Paragraph first = new Paragraph(KEY, SENTENCE);
        Paragraph second = new Paragraph(KEY, SENTENCE);

        assertEquals("Similar object should be equal", first, second);
    }

    @Test
    public void same_hash_for_equal_paragraphs() { // NOPMD
        Paragraph first = new Paragraph(KEY, SENTENCE);
        Paragraph second = new Paragraph(KEY, SENTENCE);

        assertThat(first.hashCode(), is(second.hashCode()));
    }
}
