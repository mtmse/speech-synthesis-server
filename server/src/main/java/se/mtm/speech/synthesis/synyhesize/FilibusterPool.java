package se.mtm.speech.synthesis.synyhesize;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class FilibusterPool {
    private SpeechSynthesizer speechSynthesizer;
    private int maxPoolSize;
    private long timeToLive;
    private boolean slow;
    private Queue<Filibuster> waiting;
    private List<Filibuster> all;

    FilibusterPool(Queue<Filibuster> waiting, List<Filibuster> all, int maxPoolSize) {
        this.waiting = waiting;
        this.all = all;
        this.maxPoolSize = maxPoolSize;
    }

    FilibusterPool(int maxPoolSize, long timeToLive) {
        this(null, maxPoolSize, timeToLive, false);
    }

    public FilibusterPool(SpeechSynthesizer speechSynthesizer, int maxPoolSize, long timeToLive, boolean slow) {
        this.speechSynthesizer = speechSynthesizer;
        this.maxPoolSize = maxPoolSize;
        this.timeToLive = timeToLive;
        this.slow = slow;

        waiting = new LinkedBlockingQueue<>();
        all = new LinkedList<>(); // todo thread safe!

        topUpFilibuster();
    }

    public void invalidate() {
        replaceWaitingFilibusters();
        prepareFilibusterToDie();
        topUpFilibuster();
    }

    private void replaceWaitingFilibusters() {
        while (!waiting.isEmpty()) {
            Filibuster filibuster = waiting.poll();
            all.remove(filibuster);
        }
    }

    private void prepareFilibusterToDie() {
        for (Filibuster filibuster : all) {
            filibuster.setTimeToDie(0);
        }
    }

    private void topUpFilibuster() {
        while (all.size() < maxPoolSize) {
            addFilibuster(speechSynthesizer, timeToLive, slow);
        }
    }

    private void addFilibuster(SpeechSynthesizer speechSynthesizer, long timeToLive, boolean slow) {
        if (enoughResources()) {
            Filibuster filibuster = new Filibuster(this, speechSynthesizer, timeToLive, slow);
            waiting.add(filibuster);
            all.add(filibuster);
        }
    }

    private boolean enoughResources() {
        // todo check the available resources

        return true;
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

    Filibuster getFilibuster() {
        return waiting.poll();
    }

    void returnFilibuster(Filibuster filibuster) {
        if (filibuster.isTooOld()) {
            all.remove(filibuster);
        } else {
            waiting.offer(filibuster);
        }
        topUpFilibuster();
    }
}
