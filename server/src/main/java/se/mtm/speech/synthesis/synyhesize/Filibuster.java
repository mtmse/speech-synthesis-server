package se.mtm.speech.synthesis.synyhesize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class Filibuster implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Filibuster.class);

    private final FilibusterPool pool;
    private final SpeechSynthesizer synthesizer;
    private final boolean slowPerformance;
    private long timeToDie;
    private SpeechUnit speechUnit;

    Filibuster(FilibusterPool pool, long timeToLive) {
        this(pool, null, timeToLive, true);
    }

    public Filibuster(FilibusterPool pool, SpeechSynthesizer synthesizer, long timeToLive, boolean slow) {
        this.pool = pool;
        this.synthesizer = synthesizer;
        this.timeToDie = System.currentTimeMillis() + timeToLive;
        this.slowPerformance = slow;
    }

    void setSpeechUnit(SpeechUnit speechUnit) {
        this.speechUnit = speechUnit;
    }

    @Override
    public void run() {
        SynthesizedSound synthesised = synthesize();
        synthesizer.addSynthesizedParagraph(synthesised);
        pool.returnFilibuster(this);
    }

    public boolean isTooOld() {
        return System.currentTimeMillis() > timeToDie;
    }

    public void setTimeToDie(long timeToDie) {
        this.timeToDie = timeToDie;
    }

    private SynthesizedSound synthesize() {
        simulateSlowExecution();

        String key = speechUnit.getKey();
        String sentence = speechUnit.getText();
        byte[] sound = sentence.getBytes();

        return new SynthesizedSound(key, sound);
    }

    private void simulateSlowExecution() {
        if (slowPerformance) {
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
