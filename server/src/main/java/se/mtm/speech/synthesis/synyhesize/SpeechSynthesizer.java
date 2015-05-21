package se.mtm.speech.synthesis.synyhesize;

import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class SpeechSynthesizer implements Managed {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpeechSynthesizer.class);

    private final Queue<ParagraphReady> inQue;
    private final Map<String, ParagraphReady> out;

    public SpeechSynthesizer(int capacity) {
        inQue = new LinkedBlockingQueue<>(capacity);
        out = new ConcurrentHashMap<>();
    }

    @Override
    public void start() throws Exception {
        Filibuster filibuster = new Filibuster();
        Thread thread = new Thread(filibuster);
        thread.start();
    }

    @Override
    public void stop() throws Exception {
        // todo shutdown the Filibuster pool
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

    private ParagraphReady getNext() {
        return inQue.poll();
    }

    void addSynthesizedParagraph(ParagraphReady paragraphReady) {
        out.put(paragraphReady.getKey(), paragraphReady);
    }

    public ParagraphInterface isParagraphReady(String key) {
        ParagraphReady candidate = out.get(key);
        if (candidate == null) {
            return new ParagraphNotReady();
        }

        return candidate;
    }

    public ParagraphReady popParagraph(String key) {
        ParagraphReady paragraph = (ParagraphReady) isParagraphReady(key);
        out.remove(key);

        return paragraph;
    }

    int outSize(){
        return out.size();
    }

    private class Filibuster implements Runnable {
        @Override
        public void run() {
            ParagraphReady paragraphReady;
            while ((paragraphReady = getNext()) == null) {
                pause(1);
            }

            ParagraphReady synthesized = synthesize(paragraphReady);

            simulateSlowExecution();

            addSynthesizedParagraph(synthesized);
        }

        private ParagraphReady synthesize(ParagraphReady paragraphReady) {
            String key = paragraphReady.getKey();
            String sentence = paragraphReady.getSentence();
            byte[] sound = sentence.getBytes();

            return new ParagraphReady(key, sentence, sound);
        }

        private void pause(int time) {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                LOGGER.warn(e.getMessage());
            }
        }

        private void simulateSlowExecution() {
            int min = 10;
            int max = 200;

            Random random = new Random();
            int sleepTime = random.nextInt(max) + min;

            pause(sleepTime);
        }
    }
}
