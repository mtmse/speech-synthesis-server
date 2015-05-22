package se.mtm.speech.synthesis.synyhesize;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class SpeechSynthesizerTest {
    private SpeechSynthesizer speechSynthesizer;

    @Before
    public void setUp() throws Exception {
        int capacity = 1;
        int poolSize = 1;
        int idleTime = 1;
        speechSynthesizer = new SpeechSynthesizer(capacity, poolSize, idleTime, false);
        speechSynthesizer.start();
    }

    @After
    public void tearDown() throws Exception {
        speechSynthesizer.stop();
    }

    @Test
    public void synthezise_paragraph() throws Exception { // NOPMD
        String key = "17";
        ParagraphReady paragraphReady = new ParagraphReady(key, "A sentence to be synthesized");

        boolean actualDelivery = speechSynthesizer.addParagraph(paragraphReady);

        while (speechSynthesizer.isParagraphReady(key) instanceof ParagraphNotReady) {
            Thread.sleep(1);
        }

        ParagraphReady actual = speechSynthesizer.popParagraph(key);

        assertTrue("Expected to be able to deliver a paragraphReady for synthetization", actualDelivery);
        assertThat(actual, is(paragraphReady)); // NOPMD
    }

    @Test
    public void add_too_many_sentences_and_verify_that_que_is_full() { // NOPMD
        ParagraphReady paragraphReady = new ParagraphReady("17", "A sentence to be synthesized");

        boolean firstDelivery = speechSynthesizer.addParagraph(paragraphReady);
        boolean secondDelivery = speechSynthesizer.addParagraph(paragraphReady);

        assertTrue("The que can accept one item", firstDelivery);
        assertFalse("The que can accept one item", secondDelivery);
    }

    @Test
    public void return_paragraph_not_ready_when_it_cant_be_found_in_out() { // NOPMD
        Paragraph actual = speechSynthesizer.isParagraphReady("");

        assertTrue("Expected not ready", actual instanceof ParagraphNotReady);
    }

    @Test
    public void return_paragraph_ready_when_it_can_be_found_in_out() { // NOPMD
        String key = "42";
        String sentence = "The brown fox...";
        ParagraphReady paragraph = new ParagraphReady(key, sentence);
        speechSynthesizer.addSynthesizedParagraph(paragraph);

        Paragraph actual = speechSynthesizer.isParagraphReady(key);

        assertTrue("Expected ready", actual instanceof ParagraphReady);
    }

    @Test
    public void remove_paragraph_when_it_is_popped() { // NOPMD
        String key = "42";
        String sentence = "The brown fox...";
        ParagraphReady paragraph = new ParagraphReady(key, sentence);

        assertThat(speechSynthesizer.outSize(), is(0)); // NOPMD

        speechSynthesizer.addSynthesizedParagraph(paragraph);
        assertThat(speechSynthesizer.outSize(), is(1)); // NOPMD

        speechSynthesizer.popParagraph(key);
        assertThat(speechSynthesizer.outSize(), is(0)); // NOPMD
    }

    @Test
    public void add_many_paragraphs_and_verify_that_they_are_synthesised_withinh_the_timeout_period() throws Exception { // NOPMD
        int inCapacity = 17;
        int poolSize = 5;
        int idleTime = 1;
        speechSynthesizer = new SpeechSynthesizer(inCapacity, poolSize, idleTime, false);
        speechSynthesizer.start();
        int expectedSize = 42;

        int timeout = 5000;
        long stopTime = System.currentTimeMillis() + timeout;

        addParagraphsForSynthetisation(timeout, stopTime, expectedSize);
        waitForParagraphsToBeSynthesised(timeout, stopTime, expectedSize);

        assertThat(speechSynthesizer.outSize(), is(expectedSize)); // NOPMD
        assertThat(speechSynthesizer.inQueSize(), is(0)); // NOPMD
    }

    private void addParagraphsForSynthetisation(int timeout, long stopTime, int expectedSize) throws InterruptedException {
        for (int counter = 0; counter < expectedSize; counter++) {
            String key = "" + counter;
            String sentence = "The brown fox... " + counter;
            ParagraphReady paragraph = new ParagraphReady(key, sentence);
            while (!speechSynthesizer.addParagraph(paragraph)) {
                assertTrue("The paragraphs should have been added withing " + timeout + "ms", System.currentTimeMillis() < stopTime);
                pause();
            }
        }
    }

    private void waitForParagraphsToBeSynthesised(int timeout, long stopTime, int expectedSize) throws InterruptedException {
        while (speechSynthesizer.outSize() != expectedSize) {
            assertTrue("The paragraphs should have been consumed withing " + timeout + "ms", System.currentTimeMillis() < stopTime);
            pause();
        }
    }

    private void pause() throws InterruptedException {
        Thread.sleep(5);
    }
}
