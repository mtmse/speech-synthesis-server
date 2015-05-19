package se.mtm.speech.synthesis.synyhesize;

import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class SpeechSynthesizer implements Managed {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpeechSynthesizer.class);
    private static final int CAPACITY = 17;

    private Queue<Paragraph> in;
    private Map<String, Paragraph> out;

    public SpeechSynthesizer() {
        in = new LinkedBlockingQueue<>(CAPACITY);
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

    public boolean addParagraph(Paragraph paragraph) {
        return in.add(paragraph);
    }

    private Paragraph getNext() {
        return in.poll();
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

            Paragraph synthesizedParagraph = synthesize(paragraph);

            addSynthesizedParagraph(synthesizedParagraph);
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
    }
}
