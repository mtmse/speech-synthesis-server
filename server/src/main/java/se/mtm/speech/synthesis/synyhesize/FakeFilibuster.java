package se.mtm.speech.synthesis.synyhesize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

class FakeFilibuster implements Synthesizer, Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(FakeFilibuster.class);
    private final FilibusterPool pool;
    private final SpeechSynthesizer synthesizer;
    private long timeToDie;
    private SpeechUnit speechUnit;

    FakeFilibuster(FilibusterPool pool, SpeechSynthesizer synthesizer) {
        this.pool = pool;
        this.synthesizer = synthesizer;
    }

    @Override
    public void run() {
        SynthesizedSound synthesised = synthesize();
        synthesizer.addSynthesizedParagraph(synthesised);
        pool.returnFilibuster(this);
    }

    private SynthesizedSound synthesize() {
        simulateSlowExecution();

        String key = speechUnit.getKey();
        String sentence = speechUnit.getText();
        byte[] sound = sentence.getBytes();

        return new SynthesizedSound(key, sound);
    }

    @Override
    public void setSpeechUnit(SpeechUnit speechUnit) {
        this.speechUnit = speechUnit;
    }

    @Override
    public boolean isTooOld() {
        return System.currentTimeMillis() > timeToDie;
    }

    @Override
    public void setTimeToDie(long timeToDie) {
        this.timeToDie = timeToDie;
    }

    private void simulateSlowExecution() {
        int min = 10;
        int max = 200;

        Random random = new Random();
        int sleepTime = random.nextInt(max) + min;

        pause(sleepTime);
    }

    private void pause(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            LOGGER.warn(e.getMessage());
        }
    }
}
