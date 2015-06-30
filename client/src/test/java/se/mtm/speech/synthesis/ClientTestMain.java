package se.mtm.speech.synthesis;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public final class ClientTestMain {

    private ClientTestMain() {
        // utility class
    }

    public static void main(String... args) throws Exception {
        int numberOfClients = 15;
        int testTimeInMinutes = 1;

        long executionTime = System.currentTimeMillis() + 1000 * 60 * testTimeInMinutes;
        List<LoadClient> clients = new LinkedList<>();
        while (clients.size() < numberOfClients) {
            LoadClient client = new LoadClient();
            clients.add(client);
            new Thread(client).start();
        }

        while (System.currentTimeMillis() < executionTime) {
            Thread.sleep(500);
        }

        for (LoadClient client : clients) {
            client.work = false;
        }

        for (LoadClient client : clients) {
            while (client.isRunning) {
                Thread.sleep(100);
            }
        }

        int success = 0;
        int timeout = 0;
        for (LoadClient client : clients) {
            success += client.success;
            timeout += client.timeout;
        }

        System.out.println(""); // NOPMD
        System.out.println("Summary: "); // NOPMD
        System.out.println("Success: " + success); // NOPMD
        System.out.println("Timeout: " + timeout); // NOPMD
    }

    static class LoadClient implements Runnable {
        private int success = 0;
        private int timeout = 0;
        private boolean isRunning;
        private boolean work = true;

        @Override
        public void run() {
            isRunning = true;
            SpeechClient client = getClient();
            while (work) {
                try {
                    testSynthesiseSound(client);
                } catch (IOException e) {
                    client = getClient();
                    e.printStackTrace(); // NOPMD
                }
            }
            isRunning = false;
        }

        private void testSynthesiseSound(SpeechClient client) throws IOException {
            String sentence = "Kollar att speech servern kan svara på tilltal och kan göra text till ljud.";

            SynthesizedSound sound = client.synthesise(sentence);

            if (sound.isTimeout()) {
                System.out.print("t"); // NOPMD
                timeout++;
            } else {
                System.out.print("."); // NOPMD
                success++;
            }
        }

        private SpeechClient getClient() {
            String host = "pipeutv1.mtm.se";
            int port = 9090;

            return new SpeechClient(host, port);
        }

        private void writeWavFile(SynthesizedSound sound, String fileName) throws IOException {
            File soundFile = new File(fileName);
            byte[] content = sound.getSound();
            FileUtils.writeByteArrayToFile(soundFile, content);
        }
    }
}
