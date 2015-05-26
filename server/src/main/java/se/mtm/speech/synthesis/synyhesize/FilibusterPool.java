package se.mtm.speech.synthesis.synyhesize;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public final class FilibusterPool {
    private final Queue<Filibuster> filibusters;

    public FilibusterPool(int maxPoolSize, long timeToLive) {
        this(null, maxPoolSize, timeToLive, false);
    }

    public FilibusterPool(SpeechSynthesizer speechSynthesizer, int maxPoolSize, long timeToLive, boolean slow) {
        filibusters = new LinkedBlockingQueue<>();
        for (int i = 0; i < maxPoolSize; i++) {
            Filibuster filibuster = new Filibuster(this, speechSynthesizer, timeToLive, slow);
            filibusters.add(filibuster);
        }
    }

    /**
     * Is there a Filibuster available for handling a synthesis?
     *
     * @return true if there is a Filibuster ready to synthesise,
     * false if no Filibuster is ready.
     */
    boolean peekFilibuster() {
        return filibusters.peek() != null;
    }

    Filibuster getFilibuster() {
        return filibusters.poll();
    }

    void returnFilibuster(Filibuster filibuster) {
        if (filibuster.isTooOld()) {
            return;
        }

        filibusters.offer(filibuster);
    }

}
