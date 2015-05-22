package se.mtm.speech.synthesis.synyhesize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dispatcher implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Dispatcher.class);

    private final FilibusterPool pool;
    private final SpeechSynthesizer speechSynthesizer;
    private final long idleTime;
    private boolean work = true;
    private boolean running;

    public Dispatcher(FilibusterPool pool, SpeechSynthesizer speechSynthesizer, long idleTime) {
        this.pool = pool;
        this.speechSynthesizer = speechSynthesizer;
        this.idleTime = idleTime;
    }

    public void shutDown() {
        this.work = false;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        running = true;
        while (work) {
            if (speechSynthesizer.peekNext() && pool.peekFilibuster()) {
                ParagraphReady paragraph = speechSynthesizer.getNext();
                Filibuster filibuster = pool.getFilibuster();
                if (filibuster != null) {
                    filibuster.setParagraph(paragraph);
                    Thread thread = new Thread(filibuster);
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
}
