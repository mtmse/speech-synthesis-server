package se.mtm.speech.synthesis.synthesize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.mtm.speech.synthesis.infrastructure.configuration.IdleTime;

import java.util.List;

class Dispatcher implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Dispatcher.class);

    private final FilibusterPool pool;
    private final SpeechSynthesizer speechSynthesizer;
    private final IdleTime idleTime;
    private boolean work = true;
    private boolean running;

    Dispatcher(FilibusterPool pool, SpeechSynthesizer speechSynthesizer, IdleTime idleTime) {
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
                filibuster.setSpeechUnit(speechUnit);
                Thread thread = new Thread((Runnable) filibuster);
                thread.start();
            } else {
                pause();
            }
        }

        running = false;
    }

    private void pause() {
        try {
            Thread.sleep(idleTime.getIdle());
        } catch (InterruptedException e) {
            LOGGER.warn(e.getMessage());
        }
    }

    void invalidate() {
        pool.invalidate();
    }

    public boolean isHealthy() {
        return pool.isHealthy();
    }

    public List<Synthesizer> getSynthesizers() {
        return pool.getSynthesizers();
    }
}
