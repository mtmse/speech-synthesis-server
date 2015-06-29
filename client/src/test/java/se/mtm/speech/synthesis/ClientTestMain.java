package se.mtm.speech.synthesis;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public final class ClientTestMain {

    private ClientTestMain() {
        // utility class
    }

    public static void main(String... args) throws IOException {
        String sentence = "Kollar att speech servern kan svara på tilltal och göra text till ljud.";
        String fileName = "result.wav";

        SpeechClient client = getClient();

        SynthesizedSound sound = client.synthesise(sentence);

        if (sound.isTimeout()) {
            System.out.println("Timed out"); // NOPMD
        } else {
            writeWavFile(sound, fileName);
        }
    }

    private static SpeechClient getClient() {
        String host = "pipeutv1.mtm.se";
        int port = 9090;

        return new SpeechClient(host, port);
    }

    private static void writeWavFile(SynthesizedSound sound, String fileName) throws IOException {
        File soundFile = new File(fileName);
        byte[] content = sound.getSound();
        FileUtils.writeByteArrayToFile(soundFile, content);
    }

}
