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
        int timeToLive = 1;
        int idleTime = 1;
        speechSynthesizer = new SpeechSynthesizer(capacity, poolSize, timeToLive, idleTime, false);
        speechSynthesizer.start();
    }

    @After
    public void tearDown() throws Exception {
        speechSynthesizer.stop();
    }

    @Test
    public void synthezise_paragraph() throws Exception {
        String key = "17";
        String sentence = "A sentence to be synthesized";
        byte[] sound = sentence.getBytes();
        SynthesizedSound paragraphReady = new SynthesizedSound(key, sound);
        SpeechUnit speechUnit = new SpeechUnit(key, sentence);

        boolean actualDelivery = speechSynthesizer.addSpeechUnit(speechUnit);

        while (!speechSynthesizer.isSpeechUnitReady(key)) {
            Thread.sleep(1);
        }

        SynthesizedSound actual = speechSynthesizer.getSynthesizedSound(key);

        assertTrue("Expected to be able to deliver a paragraphReady for synthetization", actualDelivery);
        assertThat(actual, is(paragraphReady));
    }

    @Test
    public void add_too_many_sentences_and_verify_that_que_is_full() {
        SpeechUnit speechUnit = new SpeechUnit("17", "A sentence to be synthesized");

        boolean firstDelivery = speechSynthesizer.addSpeechUnit(speechUnit);
        boolean secondDelivery = speechSynthesizer.addSpeechUnit(speechUnit);

        assertTrue("The que can accept one item", firstDelivery);
        assertFalse("The que can accept one item", secondDelivery);
    }

    @Test
    public void return_false_not_ready_when_it_cant_be_found_in_out() {
        assertFalse("Expected not ready", speechSynthesizer.isSpeechUnitReady(""));
    }

    @Test
    public void return_paragraph_ready_when_it_can_be_found_in_out() {
        String key = "42";
        String sentence = "The brown fox...";
        byte[] sound = sentence.getBytes();
        SynthesizedSound synthesizedSound = new SynthesizedSound(key, sound);
        speechSynthesizer.addSynthesizedParagraph(synthesizedSound);

        assertTrue("Expected ready", speechSynthesizer.isSpeechUnitReady(key));
    }

    @Test
    public void remove_paragraph_when_it_is_popped() {
        String key = "42";
        String sentence = "The brown fox...";
        byte[] sound = sentence.getBytes();
        SynthesizedSound synthesizedSound = new SynthesizedSound(key, sound);

        assertThat(speechSynthesizer.outSize(), is(0));

        speechSynthesizer.addSynthesizedParagraph(synthesizedSound);
        assertThat(speechSynthesizer.outSize(), is(1));

        speechSynthesizer.getSynthesizedSound(key);
        assertThat(speechSynthesizer.outSize(), is(0));
    }

    @Test
    public void add_many_paragraphs_and_verify_that_they_are_synthesised_withinh_the_timeout_period() throws Exception {
        int inCapacity = 17;
        int poolSize = 5;
        int timeToLive = 1;
        int idleTime = 1;
        speechSynthesizer = new SpeechSynthesizer(inCapacity, poolSize, timeToLive, idleTime, false);
        speechSynthesizer.start();
        int expectedSize = 42;

        int timeout = 5000;
        long stopTime = System.currentTimeMillis() + timeout;

        addParagraphsForSynthetisation(timeout, stopTime, expectedSize);
        waitForParagraphsToBeSynthesised(timeout, stopTime, expectedSize);

        assertThat(speechSynthesizer.outSize(), is(expectedSize));
        assertThat(speechSynthesizer.inQueSize(), is(0));
    }

    private void addParagraphsForSynthetisation(int timeout, long stopTime, int expectedSize) throws InterruptedException {
        for (int counter = 0; counter < expectedSize; counter++) {
            String key = "" + counter;
            String sentence = "The brown fox... " + counter;
            SpeechUnit speechUnit = new SpeechUnit(key, sentence);
            while (!speechSynthesizer.addSpeechUnit(speechUnit)) {
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
