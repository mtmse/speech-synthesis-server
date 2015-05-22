package se.mtm.speech.synthesis.synyhesize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class Filibuster implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Filibuster.class);

    private final FilibusterPool pool;
    private final SpeechSynthesizer synthesizer;
    private final boolean simulateSlowPerformance; // NOPMD
    private ParagraphReady paragraph;

    public Filibuster(FilibusterPool pool, SpeechSynthesizer synthesizer, boolean slow) {
        this.pool = pool;
        this.synthesizer = synthesizer;
        this.simulateSlowPerformance = slow;
    }

    void setParagraph(ParagraphReady paragraph) {
        this.paragraph = paragraph;
    }

    @Override
    public void run() {
        ParagraphReady synthesised = synthesize();
        synthesizer.addSynthesizedParagraph(synthesised);
        pool.returnFilibuster(this);
    }

    private ParagraphReady synthesize() {
        simulateSlowExecution();

        String key = paragraph.getKey();
        String sentence = paragraph.getSentence();
        byte[] sound = sentence.getBytes();

        return new ParagraphReady(key, sentence, sound);
    }

    private void simulateSlowExecution() {
        if (simulateSlowPerformance) {
            int min = 10;
            int max = 200;

            Random random = new Random();
            int sleepTime = random.nextInt(max) + min;

            pause(sleepTime);
        }
    }

    private void pause(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            LOGGER.warn(e.getMessage());
        }
    }
}
