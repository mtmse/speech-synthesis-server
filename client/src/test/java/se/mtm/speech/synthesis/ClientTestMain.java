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
    }

    static class LoadClient implements Runnable {
        private boolean work = true;

        @Override
        public void run() {
            SpeechClient client = getClient();
            while (work) {
                try {
                    testSynthesiseSound(client);
                } catch (IOException e) {
                    client = getClient();
                    e.printStackTrace(); // NOPMD
                }
            }
        }

        private void testSynthesiseSound(SpeechClient client) throws IOException {
            String sentence = "Kollar att speech servern kan svara på tilltal och kan göra text till ljud.";

            SynthesizedSound sound = client.synthesise(sentence);

            if (sound.isTimeout()) {
                System.out.print("t"); // NOPMD
            } else {
                System.out.print("."); // NOPMD
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
