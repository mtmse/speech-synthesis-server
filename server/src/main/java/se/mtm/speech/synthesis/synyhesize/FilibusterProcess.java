package se.mtm.speech.synthesis.synyhesize;

import se.mtm.speech.synthesis.infrastructure.FilibusterException;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

class FilibusterProcess {
    private final Process process;
    private final long timeout;

    FilibusterProcess(Process process, long timeout) {
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
        int length = getSoundLength();
        return readSound(length);
    }

    private int getSoundLength() {
        InputStream reader = process.getInputStream();
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

    private byte[] readSound(int length) {
        InputStream reader = process.getInputStream();
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

        return audio;
    }

    private void waitFor(InputStream inputStream) {
        int waitingTime = 20;
        long stopTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < stopTime) {
            if (isStreamReady(inputStream)) {
                return;
            }
            pause(waitingTime);
        }

        String msg = "Wait timed out. The timeout was set to " + timeout + "ms";
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
}
