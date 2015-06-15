package se.mtm.speech.synthesis.synyhesize;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.mtm.speech.synthesis.infrastructure.FilibusterException;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

class Filibuster extends Synthesizer implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Filibuster.class);

    private final FilibusterPool pool;
    private final SpeechSynthesizer synthesizer;
    private final long timeout;
    private FilibusterProcess process;
    private final String filibusterHome;
    private final String logHome;

    // used for testing
    Filibuster(FilibusterProcess process, FilibusterPool pool, SpeechSynthesizer synthesizer, String filibusterHome, String logHome, long timeout, long timeToLive) {
        super(System.currentTimeMillis() + timeToLive);
        this.process = process;
        this.pool = pool;
        this.synthesizer = synthesizer;
        this.filibusterHome = filibusterHome;
        this.logHome = logHome;
        this.timeout = timeout;
    }

    Filibuster(FilibusterPool pool, SpeechSynthesizer synthesizer, String filibusterHome, String logHome, long timeout, long timeToLive) {
        super(System.currentTimeMillis() + timeToLive);
        this.pool = pool;
        this.synthesizer = synthesizer;
        this.filibusterHome = filibusterHome;
        this.logHome = logHome;
        this.timeout = timeout;

        createFilibusterProcess();
    }

    @Override
    public void run() {
        SynthesizedSound synthesised = synthesize();
        synthesizer.addSynthesizedParagraph(synthesised);
        pool.returnFilibuster(this);
    }

    private void createFilibusterProcess() {
        String logFileName = createLogFileName();
        String[] command = getCommand(logFileName);

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

    private String createLogFileName() {
        // todo generate a good file name
        return "logFileName.log";
    }

    private SynthesizedSound synthesize() {
        String key = getSpeechUnitKey();
        process.write(getSpeechUnitText());
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

    private String[] getCommand(String logFileName) {
        String filibusterScript = filibusterHome + "filibuster.tcl";
        String logFile = logHome + logFileName;

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
