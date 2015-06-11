package se.mtm.speech.synthesis.synyhesize;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

class Filibuster implements Synthesizer, Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Filibuster.class);
    private final FilibusterPool pool;
    private final SpeechSynthesizer synthesizer;
    private final long timeout;
    private FilibusterProcess process;
    private long timeToDie;
    private SpeechUnit speechUnit;

    Filibuster(FilibusterProcess process, FilibusterPool pool, SpeechSynthesizer synthesizer, long timeout, long timeToLive) {
        this.process = process;
        this.pool = pool;
        this.synthesizer = synthesizer;
        this.timeout = timeout;
        this.timeToDie = System.currentTimeMillis() + timeToLive;
    }

    Filibuster(FilibusterPool pool, SpeechSynthesizer synthesizer, long timeout, long timeToLive) {
        this.pool = pool;
        this.synthesizer = synthesizer;
        this.timeout = timeout;
        this.timeToDie = System.currentTimeMillis() + timeToLive;

        createFilibusterProcess();
    }

    @Override
    public void run() {
        SynthesizedSound synthesised = synthesize();
        synthesizer.addSynthesizedParagraph(synthesised);
        pool.returnFilibuster(this);
    }

    @Override
    public void setSpeechUnit(SpeechUnit speechUnit) {
        this.speechUnit = speechUnit;
    }

    @Override
    public boolean isTooOld() {
        return System.currentTimeMillis() > timeToDie;
    }

    @Override
    public void setTimeToDie(long timeToDie) {
        this.timeToDie = timeToDie;
    }

    private void createFilibusterProcess() {
        String[] command = getCommand();

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();
            this.process = new FilibusterProcess(process, timeout);
            clearStartMessages(process);
            testSynthesize();
        } catch (IOException e) {
            throw new FilibusterException(e.getMessage(), e);
        }
    }

    private SynthesizedSound synthesize() {
        String key = speechUnit.getKey();
        process.write(speechUnit.getText());
        byte[] sound = process.getSound();

        return new SynthesizedSound(key, sound);
    }

    void clearStartMessages(Process process) throws IOException {
        LOGGER.info("Clear initial message in Filibuster");
        InputStream stdIn = process.getInputStream();

        int startMessages = 2;
        for (int i = 0; i < startMessages; i++) {
            int character;
            String content = "";
            while ((character = stdIn.read()) != '\n') {
                content += (char) character;
            }
            LOGGER.info(content);
        }
    }

    private void testSynthesize() {
        SpeechUnit speechUnit = new SpeechUnit("Den bruna räven hoppade över den lata junden.");
        setSpeechUnit(speechUnit);
        synthesize();
    }

    private String[] getCommand() {
        // todo configuration
        String filibusterScript = "E:\\git\\filibuster\\Synthesis\\SynthesisCore\\filibuster.tcl";
        String logFile = "E:\\git\\daisypipeline\\dmfc\\testa.log";

        List<String> cmd = new LinkedList<String>();

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
