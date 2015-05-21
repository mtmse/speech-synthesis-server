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

    private final Queue<Paragraph> inQue;
    private final Map<String, Paragraph> out;

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
     * Add a paragraph for synthesising
     *
     * @param paragraph a paragraph to be synthesised
     * @return true if the paragraph was added, false if it couldn't be added
     */
    public boolean addParagraph(Paragraph paragraph) {
        return inQue.offer(paragraph);
    }

    private Paragraph getNext() {
        return inQue.poll();
    }

    private void addSynthesizedParagraph(Paragraph paragraph) {
        out.put(paragraph.getKey(), paragraph);
    }

    public Paragraph getParagraph(String key) {
        Paragraph candidate = out.get(key);
        if (candidate == null) {
            return new ParagraphNotReady();
        }

        return candidate;
    }

    private class Filibuster implements Runnable {
        @Override
        public void run() {
            Paragraph paragraph;
            while ((paragraph = getNext()) == null) {
                pause(1);
            }

            Paragraph synthesized = synthesize(paragraph);

            simulateSlowExecution();

            addSynthesizedParagraph(synthesized);
        }

        private Paragraph synthesize(Paragraph paragraph) {
            String key = paragraph.getKey();
            String sentence = paragraph.getSentence();
            byte[] sound = sentence.getBytes();

            return new Paragraph(key, sentence, sound);
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
