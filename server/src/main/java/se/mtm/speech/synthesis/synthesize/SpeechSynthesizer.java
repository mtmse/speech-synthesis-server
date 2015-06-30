package se.mtm.speech.synthesis.synthesize;

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
    private final Map<String, SynthesizedSound> out;
    private final int filibusters;

    public SpeechSynthesizer(int inCapacity, int maxFilibusters, int minimumMemory, String filibusterHome, String logHome, long timeout, long timeToLive, long minDistance, long idleTime, boolean fake) { // NOPMD
        this.filibusters = maxFilibusters;

        int second = 1000;
        int minute = 60 * second;
        long timeoutMillis = timeout * second;
        long ttl = timeToLive * minute;
        FilibusterPool pool = new FilibusterPool(this, maxFilibusters, minimumMemory, filibusterHome, logHome, timeoutMillis, ttl, minDistance, fake);

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

    SpeechUnit getNext() {
        return inQue.poll();
    }

    void addSynthesizedParagraph(SynthesizedSound sound) {
        out.put(sound.getKey(), sound);
    }

    boolean isSpeechUnitReady(String key) {
        return out.get(key) != null;
    }

    SynthesizedSound getSynthesizedSound(String key) {
        SynthesizedSound sound = out.get(key);
        out.remove(key);

        return sound;
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

    public void invalidate() {
        dispatcher.invalidate();
    }

    public boolean isHealthy() {
        return dispatcher.isHealthy();
    }
}
