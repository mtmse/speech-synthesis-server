package se.mtm.speech.synthesis.synthesize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.mtm.speech.synthesis.infrastructure.Resources;
import se.mtm.speech.synthesis.infrastructure.configuration.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

class FilibusterPool { // NOPMD
    private static final Logger LOGGER = LoggerFactory.getLogger(FilibusterPool.class);

    private final SpeechSynthesizer speechSynthesizer;
    private final MaxFilibusters maxPoolSize;
    private final LogHome logHome;
    private final Timeout timeout;
    private final TimeToLive ttl;
    private final FakeSynthesize fake;
    private final Queue<Synthesizer> waiting;
    private final Queue<Synthesizer> all;
    private Resources resources;
    private final MinimumMemory minimumMemory;
    private final FilibusterHome filibusterHome;

    private FilibusterPool(Builder builder) {
        speechSynthesizer = builder.speechSynthesizer;
        maxPoolSize = builder.maxPoolSize;

        if (builder.minimumMemory == null) {
            minimumMemory = new MinimumMemory(0);
        } else {

            minimumMemory = builder.minimumMemory;
        }

        filibusterHome = builder.filibusterHome;
        logHome = builder.logHome;
        timeout = builder.timeout;
        ttl = builder.ttl;

        if (builder.fake == null) {
            fake = new FakeSynthesize();
        } else {
            fake = builder.fake;
        }

        if (builder.waiting == null) {
            waiting = new LinkedBlockingQueue<>();
        } else {
            waiting = builder.waiting;
        }

        if (builder.all == null) {
            all = new LinkedBlockingDeque<>();
        } else {
            all = builder.all;
        }

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
            filibuster.prepareToDie();
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
            synthesizer = new Filibuster.Builder()
                    .pool(this)
                    .synthesizer(speechSynthesizer)
                    .filibusterHome(filibusterHome)
                    .logHome(logHome)
                    .timeout(timeout)
                    .ttl(ttl)
                    .build();
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
        if (synthesizer.isTooOld() || synthesizer.unHealthy()) {
            killFilibuster(synthesizer);
        } else {
            waiting.offer(synthesizer);
        }
        topUpFilibuster();
    }

    private void killFilibuster(Synthesizer synthesizer) {
        // todo debug
        LOGGER.info("killFilibuster");
        LOGGER.info("Filibusters: " + all.size());
        all.remove(synthesizer);
        LOGGER.info("Filibusters: " + all.size());
        synthesizer.kill();
    }

    public boolean isHealthy() {
        for (Synthesizer synthesizer : all) {
            if (!synthesizer.isHealthy()) {
                return false;
            }
        }
        return true;
    }

    public List<Synthesizer> getSynthesizers() {
        List<Synthesizer> synthesizers = new LinkedList<>();
        synthesizers.addAll(all);

        return synthesizers;
    }

    public static class Builder {
        private SpeechSynthesizer speechSynthesizer; // NOPMD
        private MaxFilibusters maxPoolSize; // NOPMD
        Queue<Synthesizer> waiting; // NOPMD
        Queue<Synthesizer> all; // NOPMD
        private MinimumMemory minimumMemory; // NOPMD
        private FilibusterHome filibusterHome; // NOPMD
        private LogHome logHome; // NOPMD
        private Timeout timeout; // NOPMD
        private TimeToLive ttl; // NOPMD
        private FakeSynthesize fake; // NOPMD

        public Builder speechSynthesizer(SpeechSynthesizer speechSynthesizer) {
            this.speechSynthesizer = speechSynthesizer;
            return this;
        }

        public Builder maxPoolSize(MaxFilibusters maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
            return this;
        }

        public Builder waiting(Queue<Synthesizer> waiting) {
            this.waiting = waiting;
            return this;
        }

        public Builder all(Queue<Synthesizer> all) {
            this.all = all;
            return this;
        }

        public Builder minimumMemory(MinimumMemory minimumMemory) {
            this.minimumMemory = minimumMemory;
            return this;
        }

        public Builder filibusterHome(FilibusterHome filibusterHome) {
            this.filibusterHome = filibusterHome;
            return this;
        }

        public Builder logHome(LogHome logHome) {
            this.logHome = logHome;
            return this;
        }

        public Builder timeout(Timeout timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder ttl(TimeToLive ttl) {
            this.ttl = ttl;
            return this;
        }

        public Builder fake(FakeSynthesize fake) {
            this.fake = fake;
            return this;
        }

        public FilibusterPool build() {
            return new FilibusterPool(this); // NOPMD
        }

    }
}
