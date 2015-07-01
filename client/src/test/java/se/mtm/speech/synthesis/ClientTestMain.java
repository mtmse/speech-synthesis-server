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
        int testTimeInMinutes = 5;

        List<LoadClient> clients = startClients(numberOfClients);
        run(testTimeInMinutes);
        shutDown(clients);
        waitForClientsToShutdown(clients);
        printSummary(clients);
    }

    private static List<LoadClient> startClients(int numberOfClients) {
        List<LoadClient> clients = new LinkedList<>();
        while (clients.size() < numberOfClients) {
            LoadClient client = new LoadClient();
            clients.add(client);
            new Thread(client).start();
        }
        return clients;
    }

    private static void run(int testTimeInMinutes) throws InterruptedException {
        long executionTime = System.currentTimeMillis() + 1000 * 60 * testTimeInMinutes;
        while (System.currentTimeMillis() < executionTime) {
            Thread.sleep(500);
        }
    }

    private static void shutDown(List<LoadClient> clients) {
        for (LoadClient client : clients) {
            client.work = false;
        }
    }

    private static void waitForClientsToShutdown(List<LoadClient> clients) throws InterruptedException {
        for (LoadClient client : clients) {
            while (client.isRunning) {
                Thread.sleep(100);
            }
        }
    }

    private static void printSummary(List<LoadClient> clients) {
        int success = 0;
        int timeout = 0;
        int notAccepted = 0;

        for (LoadClient client : clients) {
            success += client.success;
            timeout += client.timeout;
            notAccepted += client.notAccepted;
        }

        System.out.println(""); // NOPMD
        System.out.println(""); // NOPMD
        System.out.println("Summary "); // NOPMD
        System.out.println("Success: " + success); // NOPMD
        System.out.println("Timeout: " + timeout); // NOPMD
        System.out.println("Not accepted: " + notAccepted); // NOPMD
    }

    static class LoadClient implements Runnable {
        private int success = 0;
        private int timeout = 0;
        private int notAccepted = 0;
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

            if (sound.isNotAccepted()) {
                System.out.print("n"); // NOPMD
                notAccepted++;
                pause();
                return;
            }

            if (sound.isTimeout()) {
                System.out.print("t"); // NOPMD
                timeout++;
                return;
            }

            System.out.print("."); // NOPMD
            success++;
        }

        private void pause() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace(); // NOPMD
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
