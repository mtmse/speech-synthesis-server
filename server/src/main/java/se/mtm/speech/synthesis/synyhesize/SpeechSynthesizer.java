package se.mtm.speech.synthesis.synyhesize;

import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class SpeechSynthesizer implements Managed {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpeechSynthesizer.class);
    private final Dispatcher dispatcher;
    private final Queue<ParagraphReady> inQue;
    private final Map<String, ParagraphReady> out;
    private final int filibusters;

    public SpeechSynthesizer(int inCapacity, int filibusters, long idleTime, boolean slow) {
        this.filibusters = filibusters;

        FilibusterPool pool = new FilibusterPool(this, filibusters, slow);

        dispatcher = new Dispatcher(pool, this, idleTime);
        inQue = new LinkedBlockingQueue<>(inCapacity);
        out = new ConcurrentHashMap<>();
    }

    @Override
    public void start() throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Starting speech synthesis server with " + filibusters + " Filibusters"); // NOPMD
        }

        Thread thread = new Thread(dispatcher);
        thread.start();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Started speech synthesis server"); // NOPMD
        }
    }

    @Override
    public void stop() throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Shutting down speech synthesis server"); // NOPMD
        }

        dispatcher.shutDown();
        while (dispatcher.isRunning()) {
            pause();
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Speech synthesiser shut down"); // NOPMD
        }
    }

    /**
     * Add a paragraphReady for synthesising
     *
     * @param paragraphReady a paragraphReady to be synthesised
     * @return true if the paragraphReady was added, false if it couldn't be added
     */
    public boolean addParagraph(ParagraphReady paragraphReady) {
        return inQue.offer(paragraphReady);
    }

    boolean peekNext() {
        return inQue.peek() != null;
    }

    ParagraphReady getNext() {
        return inQue.poll();
    }

    void addSynthesizedParagraph(ParagraphReady paragraphReady) {
        out.put(paragraphReady.getKey(), paragraphReady);
    }

    Paragraph isParagraphReady(String key) {
        ParagraphReady candidate = out.get(key);
        if (candidate == null) {
            return new ParagraphNotReady();
        }

        return candidate;
    }

    ParagraphReady popParagraph(String key) {
        ParagraphReady paragraph = (ParagraphReady) isParagraphReady(key);
        out.remove(key);

        return paragraph;
    }

    int outSize() {
        return out.size();
    }

    public int inQueSize() {
        return inQue.size();
    }

    private void pause() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            LOGGER.warn(e.getMessage());
        }
    }
}
