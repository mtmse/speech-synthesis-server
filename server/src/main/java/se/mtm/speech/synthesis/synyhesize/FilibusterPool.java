package se.mtm.speech.synthesis.synyhesize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.mtm.speech.synthesis.infrastructure.Resources;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

class FilibusterPool {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilibusterPool.class);

    private SpeechSynthesizer speechSynthesizer;
    private int maxPoolSize;
    private long timeout;
    private long timeToLive;
    private boolean fake;
    private Queue<Synthesizer> waiting;
    private Queue<Synthesizer> all;
    private Resources resources;
    private int minimumMemory;
    private String filibusterHome;

    FilibusterPool(int maxPoolSize, long timeToLive) {
        this(null, maxPoolSize, 2, "not defined", 30000, timeToLive, true);
    }

    FilibusterPool(Queue<Synthesizer> waiting, Queue<Synthesizer> all, int maxPoolSize) {
        this.waiting = waiting;
        this.all = all;
        this.maxPoolSize = maxPoolSize;
        this.fake = true;
    }

    public FilibusterPool(SpeechSynthesizer speechSynthesizer, int maxPoolSize, int minimumMemory, String filibusterHome, long timeout, long timeToLive, boolean fake) {
        this.speechSynthesizer = speechSynthesizer;
        this.maxPoolSize = maxPoolSize;
        this.minimumMemory = minimumMemory;
        this.filibusterHome = filibusterHome;
        this.timeout = timeout;
        this.timeToLive = timeToLive;
        this.fake = fake;

        waiting = new LinkedBlockingQueue<>();
        all = new LinkedBlockingDeque<>();

        topUpFilibuster();
    }

    public void invalidate() {
        replaceWaitingFilibusters();
        prepareFilibusterToDie();
        topUpFilibuster();
    }

    private void replaceWaitingFilibusters() {
        while (!waiting.isEmpty()) {
            Synthesizer filibuster = waiting.poll();
            all.remove(filibuster);
        }
    }

    private void prepareFilibusterToDie() {
        for (Synthesizer filibuster : all) {
            filibuster.setTimeToDie(0);
        }
    }

    private void topUpFilibuster() {
        LOGGER.info("Topping up with Synthesizers");
        while (all.size() < maxPoolSize && enoughResources()) {
            addFilibuster(speechSynthesizer, filibusterHome, timeout, timeToLive, fake);
        }
    }

    private void addFilibuster(SpeechSynthesizer speechSynthesizer, String filibusterHome, long timeout, long timeToLive, boolean fake) {
        Synthesizer synthesizer;

        if (fake) {
            synthesizer = new FakeFilibuster(this, speechSynthesizer);
        } else {
            synthesizer = new Filibuster(this, speechSynthesizer, filibusterHome, timeout, timeToLive);
        }
        waiting.add(synthesizer);
        all.add(synthesizer);
    }

    private boolean enoughResources() {
        if (resources == null) {
            resources = new Resources();
        }

        int availableMemory = resources.getAvailableMemory();
        String message = "Have " + availableMemory + " Gb memory free";
        LOGGER.info(message);

        return availableMemory > minimumMemory;
    }

    /**
     * Is there a Filibuster available for handling a synthesis?
     *
     * @return true if there is a Filibuster ready to synthesise,
     * false if no Filibuster is available.
     */
    boolean peekFilibuster() {
        return waiting.peek() != null;
    }

    Synthesizer getSynthesizer() {
        return waiting.poll();
    }

    void returnFilibuster(Synthesizer synthesizer) {
        if (synthesizer.isTooOld()) {
            all.remove(synthesizer);
        } else {
            waiting.offer(synthesizer);
        }
        topUpFilibuster();
    }
}
