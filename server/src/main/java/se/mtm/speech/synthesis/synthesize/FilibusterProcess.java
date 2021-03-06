package se.mtm.speech.synthesis.synthesize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.mtm.speech.synthesis.infrastructure.FilibusterException;
import se.mtm.speech.synthesis.infrastructure.configuration.Timeout;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

class FilibusterProcess {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilibusterProcess.class);
    private final Process process;
    private final Timeout timeout;

    FilibusterProcess(Process process, Timeout timeout) {
        this.process = process;
        this.timeout = timeout;
    }

    void write(String text) {
        try {
            BufferedOutputStream out = new BufferedOutputStream(process.getOutputStream());
            OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");

            writer.write(text);
            writer.write("\n");
            writer.flush();
        } catch (IOException e) {
            throw new FilibusterException(e.getMessage(), e);
        }
    }

    byte[] getSound() {
        InputStream reader = process.getInputStream();
        waitFor(reader);
        int length = getSoundLength(reader);
        return readSound(reader, length);
    }

    private int getSoundLength(InputStream reader) {
        String size = "";
        int currentByte;

        try {
            while ((currentByte = reader.read()) != '\n') {
                int endOfFile = -1;
                if (currentByte == endOfFile) {
                    String msg = "Connection to the TTS was lost unexpectedly";
                    throw new FilibusterException(msg);
                }
                size += (char) currentByte;
                waitFor(reader);
            }
        } catch (IOException e) {
            throw new FilibusterException(e.getMessage(), e);
        }

        return Integer.parseInt(size);
    }

    private byte[] readSound(InputStream reader, int length) {
        String message = "Read " + length + " bytes from Filibuster";
        LOGGER.info(message);

        byte[] audio = new byte[length];
        int remaining = length;
        while (remaining > 0) {
            waitFor(reader);
            int start = length - remaining;

            try {
                int read = reader.read(audio, start, remaining);
                remaining -= read;
            } catch (IOException e) {
                throw new FilibusterException(e);
            }
        }

        message = "The length of the returned byte array with sound is " + audio.length;
        LOGGER.info(message);

        return audio;
    }

    private void waitFor(InputStream inputStream) {
        int waitingTime = 20;
        long stopTime = System.currentTimeMillis() + timeout.getTimeoutMilliseconds();
        while (System.currentTimeMillis() < stopTime) {
            if (isStreamReady(inputStream)) {
                return;
            }
            pause(waitingTime);
        }

        String msg = "Wait timed out. The timeout was set to " + timeout + "s";
        throw new FilibusterException(msg);
    }

    private boolean isStreamReady(InputStream inputStream) {
        try {
            return inputStream.available() > 0;
        } catch (IOException e) {
            throw new FilibusterException(e);
        }
    }

    private void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new FilibusterException(e);
        }
    }

    public boolean isHealthy() {
        return process.isAlive();
    }

    public void kill() {
        process.destroyForcibly();
    }
}
