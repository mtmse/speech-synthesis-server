package se.mtm.speech.synthesis.synthesize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Dispatcher implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Dispatcher.class);

    private final FilibusterPool pool;
    private final SpeechSynthesizer speechSynthesizer;
    private final long idleTime;
    private boolean work = true;
    private boolean running;

    Dispatcher(FilibusterPool pool, SpeechSynthesizer speechSynthesizer, long idleTime) {
        this.pool = pool;
        this.speechSynthesizer = speechSynthesizer;
        this.idleTime = idleTime;
    }

    void shutDown() {
        this.work = false;
    }

    boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        running = true;
        while (work) {
            if (speechSynthesizer.peekNext() && pool.peekFilibuster()) {
                SpeechUnit speechUnit = speechSynthesizer.getNext();
                Synthesizer filibuster = pool.getSynthesizer();
                if (filibuster != null) {
                    filibuster.setSpeechUnit(speechUnit);
                    Thread thread = new Thread((Runnable) filibuster);
                    thread.start();
                }
            } else {
                pause();
            }
        }

        running = false;
    }

    private void pause() {
        try {
            Thread.sleep(idleTime);
        } catch (InterruptedException e) {
            LOGGER.warn(e.getMessage());
        }
    }

    void invalidate() {
        pool.invalidate();
    }
}
