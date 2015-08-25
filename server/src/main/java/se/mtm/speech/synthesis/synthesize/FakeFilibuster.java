package se.mtm.speech.synthesis.synthesize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

class FakeFilibuster extends Synthesizer implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(FakeFilibuster.class);
    private final FilibusterPool pool;
    private final SpeechSynthesizer synthesizer;

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

        String key = getSpeechUnitKey();
        String sentence = getSpeechUnitText();
        byte[] sound = sentence.getBytes();

        return new SynthesizedSound.Builder()
                .key(key)
                .sound(sound)
                .build();
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

    @Override
    boolean isHealthy() {
        return true;
    }

    @Override
    void kill() {
        // no implementation needed
    }
}
