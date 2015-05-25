package se.mtm.speech.synthesis.synyhesize;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ParagraphTest {
    private final static String KEY = "42";
    private final static String SENTENCE = "A brown fox";

    @Test
    public void set_key_and_sentence() {
        ParagraphReady paragraphReady = new ParagraphReady(KEY, SENTENCE);

        assertThat(paragraphReady.getKey(), is(KEY));
        assertThat(paragraphReady.getSentence(), is(SENTENCE));
    }

    @Test
    public void set_key_sentence_and_sound() {
        byte[] sound = SENTENCE.getBytes();

        ParagraphReady paragraphReady = new ParagraphReady(KEY, SENTENCE, sound);

        assertThat(paragraphReady.getKey(), is(KEY));
        assertThat(paragraphReady.getSentence(), is(SENTENCE));
        assertThat(paragraphReady.getSound(), is(sound));
    }

    @Test
    public void equal_object_are_equal() {
        ParagraphReady first = new ParagraphReady(KEY, SENTENCE);
        ParagraphReady second = new ParagraphReady(KEY, SENTENCE);

        assertEquals("Similar object should be equal", first, second);
    }

    @Test
    public void same_hash_for_equal_paragraphs() {
        ParagraphReady first = new ParagraphReady(KEY, SENTENCE);
        ParagraphReady second = new ParagraphReady(KEY, SENTENCE);

        assertThat(first.hashCode(), is(second.hashCode()));
    }
}
