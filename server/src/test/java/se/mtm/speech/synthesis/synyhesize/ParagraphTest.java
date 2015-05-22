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
        ParagraphReady paragraphReady = new ParagraphReady(KEY, SENTENCE);

        assertThat(paragraphReady.getKey(), is(KEY)); // NOPMD
        assertThat(paragraphReady.getSentence(), is(SENTENCE)); // NOPMD
    }

    @Test
    public void set_key_sentence_and_sound() { // NOPMD
        byte[] sound = SENTENCE.getBytes();

        ParagraphReady paragraphReady = new ParagraphReady(KEY, SENTENCE, sound);

        assertThat(paragraphReady.getKey(), is(KEY)); // NOPMD
        assertThat(paragraphReady.getSentence(), is(SENTENCE)); // NOPMD
        assertThat(paragraphReady.getSound(), is(sound)); // NOPMD
    }

    @Test
    public void equal_object_are_equal() { // NOPMD
        ParagraphReady first = new ParagraphReady(KEY, SENTENCE);
        ParagraphReady second = new ParagraphReady(KEY, SENTENCE);

        assertEquals("Similar object should be equal", first, second);
    }

    @Test
    public void same_hash_for_equal_paragraphs() { // NOPMD
        ParagraphReady first = new ParagraphReady(KEY, SENTENCE);
        ParagraphReady second = new ParagraphReady(KEY, SENTENCE);

        assertThat(first.hashCode(), is(second.hashCode())); // NOPMD
    }
}
