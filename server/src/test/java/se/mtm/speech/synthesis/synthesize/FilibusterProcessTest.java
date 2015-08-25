package se.mtm.speech.synthesis.synthesize;

import org.apache.commons.lang3.SystemUtils;
import se.mtm.speech.synthesis.infrastructure.configuration.TimeToLive;
import se.mtm.speech.synthesis.infrastructure.configuration.Timeout;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public final class FilibusterProcessTest {

    private FilibusterProcessTest() {
    }

    /**
     * Test to use the sound synthesizer
     */
    public static void main(String... args) throws Exception {
        String testSentence = "Hej!";
        String fileName = "01.wav";

        Process process = getProcess();
        Timeout timeout = new Timeout(30);
        TimeToLive ttl = new TimeToLive(30);

        FilibusterProcess filibusterProcess = new FilibusterProcess(process, timeout);

        Filibuster filibuster = new Filibuster.Builder()
                .timeout(timeout)
                .ttl(ttl)
                .build();

        filibuster.clearStartMessages(process);

        filibusterProcess.write(testSentence);
        byte[] sound = filibusterProcess.getSound();
        writeSoundFile(sound, fileName);
    }

    private static void writeSoundFile(byte[] sound, String fileName) {
        try {
            FileOutputStream out = new FileOutputStream(fileName);
            out.write(sound);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Process getProcess() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(getCommand());
        processBuilder.redirectErrorStream(true);

        return processBuilder.start();
    }

    private static String[] getCommand() {
        String filibusterScript = "E:\\git\\filibuster\\Synthesis\\SynthesisCore\\filibuster.tcl";
        String logFile = "E:\\git\\daisypipeline\\dmfc\\testa.log";

        List<String> cmd = new LinkedList<>();

        if (SystemUtils.IS_OS_WINDOWS) {
            cmd.add("cmd");
            cmd.add("/c");
            cmd.add("tclsh");
        }

        cmd.add(filibusterScript);
        cmd.add("-lang");
        cmd.add("sv");
        cmd.add("-mode");
        cmd.add("batch");
        cmd.add("-textfile");
        cmd.add("-");
        cmd.add("-outdir");
        cmd.add("-");
        cmd.add("-rate");
        cmd.add("22050");
        cmd.add("-log");
        cmd.add(logFile);
        cmd.add("-debug");
        cmd.add("1");
        cmd.add("-print_header");
        cmd.add("FILESIZE");

        return cmd.toArray(new String[cmd.size()]);
    }
}
