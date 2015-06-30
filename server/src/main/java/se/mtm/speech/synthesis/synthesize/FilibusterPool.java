package se.mtm.speech.synthesis.synthesize;

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
    private String logHome;
    private long timeout;
    private long timeToLive;
    private long minDistance;
    private boolean fake;
    private Queue<Synthesizer> waiting;
    private Queue<Synthesizer> all;
    private Resources resources;
    private int minimumMemory;
    private String filibusterHome;
    private long lastTopUp = 0;

    FilibusterPool(int maxPoolSize, long timeToLive, long minDistance) {
        this(null, maxPoolSize, 2, "not defined", "not used", 30000, timeToLive, minDistance, true);
    }

    FilibusterPool(Queue<Synthesizer> waiting, Queue<Synthesizer> all, int maxPoolSize) {
        this.waiting = waiting;
        this.all = all;
        this.maxPoolSize = maxPoolSize;
        this.fake = true;
    }

    public FilibusterPool(SpeechSynthesizer speechSynthesizer, int maxPoolSize, int minimumMemory, String filibusterHome, String logHome, long timeout, long timeToLive, long minDistance, boolean fake) {
        this.speechSynthesizer = speechSynthesizer;
        this.maxPoolSize = maxPoolSize;
        this.minimumMemory = minimumMemory;
        this.filibusterHome = filibusterHome;
        this.logHome = logHome;
        this.timeout = timeout;
        this.timeToLive = timeToLive;
        this.minDistance = minDistance;
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
            Synthesizer synthesizer = waiting.poll();
            killFilibuster(synthesizer);
        }
    }

    private void prepareFilibusterToDie() {
        for (Synthesizer filibuster : all) {
            filibuster.setTimeToDie(0);
        }
    }

    private void topUpFilibuster() {
        if (shouldAddFilibuster()) {
            LOGGER.info("Topping up with a new Filibuster");
            addFilibuster(speechSynthesizer, filibusterHome, logHome, timeout, timeToLive, fake);
            lastTopUp = System.currentTimeMillis();
        }
    }

    private boolean shouldAddFilibuster() {
        boolean wantMore = all.size() < maxPoolSize;
        boolean enoughResources = enoughResources();
        boolean enoughTime = System.currentTimeMillis() > (lastTopUp + minDistance);

        boolean addMore = wantMore && enoughResources && enoughTime;

        String message = "Have " + all.size() + " filibusters. " +
                "Max pool is: " + maxPoolSize + ". " +
                "Want more: " + wantMore + ". " +
                "Have " + resources.getAvailableMemory() + " Gb memory. " +
                "Want minimum " + minimumMemory + "Gb memory. " +
                "Have enough: " + enoughResources + " " +
                "Last top up: " + lastTopUp + ", " +
                "Min distance: " + minDistance + ", " +
                "current time: " + System.currentTimeMillis() + " " +
                "time difference: " + (System.currentTimeMillis() - lastTopUp) + "ms. " +
                "enough time difference: " + enoughTime + ". " +
                "Add more filibusters: " + addMore;

        LOGGER.info(message);

        return addMore;
    }

    private void addFilibuster(SpeechSynthesizer speechSynthesizer, String filibusterHome, String logHome, long timeout, long timeToLive, boolean fake) {
        Synthesizer synthesizer;

        if (fake) {
            synthesizer = new FakeFilibuster(this, speechSynthesizer);
        } else {
            synthesizer = new Filibuster(this, speechSynthesizer, filibusterHome, logHome, timeout, timeToLive);
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
            killFilibuster(synthesizer);
        } else {
            waiting.offer(synthesizer);
        }
        topUpFilibuster();
    }

    private void killFilibuster(Synthesizer synthesizer) {
        synthesizer.kill();
        all.remove(synthesizer);
    }

    public boolean isHealthy() {
        for (Synthesizer synthesizer : all) {
            if (!synthesizer.isHealthy()) {
                return false;
            }
        }
        return true;
    }
}
