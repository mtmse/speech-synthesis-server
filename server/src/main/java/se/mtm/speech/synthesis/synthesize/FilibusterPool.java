package se.mtm.speech.synthesis.synthesize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.mtm.speech.synthesis.infrastructure.Resources;
import se.mtm.speech.synthesis.infrastructure.configuration.*;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

class FilibusterPool {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilibusterPool.class);

    private SpeechSynthesizer speechSynthesizer;
    private MaxFilibusters maxPoolSize;
    private LogHome logHome;
    private Timeout timeout;
    private TimeToLive ttl;
    private FakeSynthesize fake;
    private Queue<Synthesizer> waiting;
    private Queue<Synthesizer> all;
    private Resources resources;
    private MinimumMemory minimumMemory;
    private FilibusterHome filibusterHome;

    FilibusterPool(MaxFilibusters maxPoolSize, TimeToLive ttl) {
        this(null, maxPoolSize, new MinimumMemory(2), new FilibusterHome("not defined"), new LogHome("not used"),
                new Timeout(30), ttl, new FakeSynthesize(true));
    }

    FilibusterPool(Queue<Synthesizer> waiting, Queue<Synthesizer> all, MaxFilibusters maxPoolSize) {
        this.waiting = waiting;
        this.all = all;
        this.maxPoolSize = maxPoolSize;
        this.minimumMemory = new MinimumMemory(0);
        this.fake = new FakeSynthesize(true);
    }

    public FilibusterPool(SpeechSynthesizer speechSynthesizer, MaxFilibusters maxPoolSize, MinimumMemory minimumMemory,
                          FilibusterHome filibusterHome, LogHome logHome, Timeout timeout, TimeToLive ttl, FakeSynthesize fake) {
        this.speechSynthesizer = speechSynthesizer;
        this.maxPoolSize = maxPoolSize;
        this.minimumMemory = minimumMemory;
        this.filibusterHome = filibusterHome;
        this.logHome = logHome;
        this.timeout = timeout;
        this.ttl = ttl;
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
        synchronized (this) {
            if (shouldAddFilibuster()) {
                LOGGER.info("Topping up with a new Filibuster");
                addFilibuster(speechSynthesizer, filibusterHome, logHome, timeout, ttl, fake);
            }
        }
    }

    private boolean shouldAddFilibuster() {
        boolean wantMore = all.size() < maxPoolSize.getMax();
        boolean enoughResources = enoughResources();

        boolean addMore = wantMore && enoughResources;

        String message = all.size() + " filibusters, " +
                "max is: " + maxPoolSize + ", " +
                "Want more: " + wantMore + ". " +
                "Have " + resources.getAvailableMemory() + " Gb, " +
                "Want " + minimumMemory + "Gb, " +
                "Have enough: " + enoughResources + " " +
                "Add more filibusters: " + addMore;

        LOGGER.info(message);

        return addMore;
    }

    private void addFilibuster(SpeechSynthesizer speechSynthesizer, FilibusterHome filibusterHome, LogHome logHome, Timeout timeout, TimeToLive ttl, FakeSynthesize fake) {
        Synthesizer synthesizer;

        if (fake.isFake()) {
            synthesizer = new FakeFilibuster(this, speechSynthesizer);
        } else {
            synthesizer = new Filibuster(this, speechSynthesizer, filibusterHome, logHome, timeout, ttl);
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

        return availableMemory > minimumMemory.getMin();
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
        if (waiting.isEmpty()) {
            topUpFilibuster();
            return waiting.poll();
        }

        Synthesizer synthesizer = getFirstHealthySynthesizer();

        if (synthesizer == null) {
            topUpFilibuster();
            synthesizer = waiting.poll();
        }

        return synthesizer;
    }

    private Synthesizer getFirstHealthySynthesizer() {
        Synthesizer next = waiting.poll();
        while (next != null && !next.isHealthy()) {
            killFilibuster(next);
            next = waiting.poll();
        }

        return next;
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
