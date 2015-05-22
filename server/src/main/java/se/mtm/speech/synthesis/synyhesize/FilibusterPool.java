package se.mtm.speech.synthesis.synyhesize;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class FilibusterPool {
    private final Queue<Filibuster> filibusters;

    public FilibusterPool(SpeechSynthesizer speechSynthesizer, int initialPoolSize, boolean slow) {
        filibusters = new LinkedBlockingQueue<>();
        for (int i = 0; i < initialPoolSize; i++) {
            filibusters.add(new Filibuster(this, speechSynthesizer, slow));
        }
    }

    boolean peekFilibuster() {
        return filibusters.peek() != null;
    }

    Filibuster getFilibuster() {
        return filibusters.poll();
    }

    void returnFilibuster(Filibuster filibuster) {
        filibusters.offer(filibuster);
    }

}
