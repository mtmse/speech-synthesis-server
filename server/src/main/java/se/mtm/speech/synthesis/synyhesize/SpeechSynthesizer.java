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
    private final Queue<SpeechUnit> inQue;
    private final Map<String, ParagraphReady> out;
    private final int filibusters;

    public SpeechSynthesizer(int inCapacity, int maxFilibusters, long timeToLive, long idleTime, boolean slow) {
        this.filibusters = maxFilibusters;

        int minute = 60 * 1000;
        long ttl = timeToLive * minute;
        FilibusterPool pool = new FilibusterPool(this, maxFilibusters, ttl, slow);

        dispatcher = new Dispatcher(pool, this, idleTime);
        inQue = new LinkedBlockingQueue<>(inCapacity);
        out = new ConcurrentHashMap<>();
    }

    @Override
    public void start() throws Exception {
        String msg = "Starting speech synthesis server with " + filibusters + " Filibusters";
        LOGGER.info(msg);

        Thread thread = new Thread(dispatcher);
        thread.start();

        LOGGER.info("Started speech synthesis server");
    }

    @Override
    public void stop() throws Exception {
        LOGGER.info("Shutting down speech synthesis server");

        dispatcher.shutDown();
        while (dispatcher.isRunning()) {
            pause();
        }

        LOGGER.info("Speech synthesiser shut down");
    }

    /**
     * Add a speech unit for synthesising
     *
     * @param speechUnit a speech unit to be synthesised
     * @return true if the paragraphReady was added, false if it couldn't be added
     */
    public boolean addSpeechUnit(SpeechUnit speechUnit) {
        return inQue.offer(speechUnit);
    }

    /**
     * Is there a job ready to be handled?
     *
     * @return true if there is a job waiting
     */
    boolean peekNext() {
        return inQue.peek() != null;
    }

    ParagraphReady getNext() {
        SpeechUnit speechUnit = inQue.poll();
        ParagraphReady next = new ParagraphReady(speechUnit.getKey(), speechUnit.getText());

        return next;
    }

    void addSynthesizedParagraph(ParagraphReady paragraphReady) {
        out.put(paragraphReady.getKey(), paragraphReady);
    }

    boolean isSpeechUnitReady(String key) {
        return out.get(key) != null;
    }

    SynthesizedSound getSynthesizedSound(String key) {
        ParagraphReady paragraph = out.get(key);
        out.remove(key);

        return new SynthesizedSound(paragraph.getSound());
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
