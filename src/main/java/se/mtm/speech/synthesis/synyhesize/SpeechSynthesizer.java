package se.mtm.speech.synthesis.synyhesize;

import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Random;

public class SpeechSynthesizer implements Managed {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpeechSynthesizer.class);

    private Random random = new Random();

    @Override
    public void start() throws Exception {
        // todo initiate the Filibuster pool
    }

    @Override
    public void stop() throws Exception {
        // todo shutdown the Filibuster pool
    }

    public Synthesized synthesize(String paragraph) {
        simulateExecutionTime();

        byte[] simulatedSound = paragraph.getBytes(Charset.forName("UTF-8"));
        return new Synthesized(paragraph, simulatedSound);
    }

    private void simulateExecutionTime() {
        int min = 100;
        int max = 1000;

        int sleepTime = random.nextInt(max) + min;

        LOGGER.info("Simulating slow behaviour by sleeping " + sleepTime + "ms");
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            LOGGER.warn(e.getMessage());
        }
    }
}
