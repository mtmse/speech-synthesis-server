package se.mtm.speech.synthesis.synthesize;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.mtm.speech.synthesis.infrastructure.FilibusterException;
import se.mtm.speech.synthesis.infrastructure.LogName;
import se.mtm.speech.synthesis.infrastructure.configuration.FilibusterHome;
import se.mtm.speech.synthesis.infrastructure.configuration.LogHome;
import se.mtm.speech.synthesis.infrastructure.configuration.TimeToLive;
import se.mtm.speech.synthesis.infrastructure.configuration.Timeout;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

class Filibuster extends Synthesizer implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Filibuster.class);

    private final FilibusterPool pool;
    private final SpeechSynthesizer synthesizer;
    private final Timeout timeout;
    private FilibusterProcess process;
    private final FilibusterHome filibusterHome;
    private final LogHome logHome;

    // used for testing
    Filibuster(FilibusterProcess process, FilibusterPool pool, SpeechSynthesizer synthesizer, FilibusterHome filibusterHome, LogHome logHome, Timeout timeout, TimeToLive ttl) {
        super(System.currentTimeMillis() + ttl.getTtlInMilliseconds());
        this.process = process;
        this.pool = pool;
        this.synthesizer = synthesizer;
        this.filibusterHome = filibusterHome;
        this.logHome = logHome;
        this.timeout = timeout;
    }

    Filibuster(FilibusterPool pool, SpeechSynthesizer synthesizer, FilibusterHome filibusterHome, LogHome logHome, Timeout timeout, TimeToLive ttl) {
        super(System.currentTimeMillis() + ttl.getTtlInMilliseconds());
        this.pool = pool;
        this.synthesizer = synthesizer;
        this.filibusterHome = filibusterHome;
        this.logHome = logHome;
        this.timeout = timeout;

        createFilibusterProcess();
    }

    @Override
    public void run() {
        try {
            SynthesizedSound synthesised = synthesize();
            synthesizer.addSynthesizedParagraph(synthesised);
            pool.returnFilibuster(this);
        } catch (FilibusterException e) {
            kill();
            LOGGER.warn(e.getMessage());
        }
    }

    private void createFilibusterProcess() {
        String logFileName = createLogFileName();
        String[] command = getCommand(logFileName);
        logStartCommand(command);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();
            this.process = new FilibusterProcess(process, timeout);
            clearStartMessages(process);
            testSynthesize();
        } catch (Exception e) {
            throw new FilibusterException(e.getMessage(), e);
        }
    }

    private void logStartCommand(String... command) {
        String commandString = "";
        for (String part : command) {
            commandString += part + " ";
        }
        String message = "Starting a Filubuster with the command <" + commandString + ">";
        LOGGER.info(message);
    }

    private String createLogFileName() {
        return LogName.getLogFileName(logHome.getHome());
    }

    private SynthesizedSound synthesize() {
        String key = getSpeechUnitKey();
        process.write(getSpeechUnitText());
        byte[] sound = process.getSound();

        return new SynthesizedSound.Builder()
                .key(key)
                .sound(sound)
                .build();
    }

    void clearStartMessages(Process process) throws IOException {
        LOGGER.info("Clear initial messages from Filibuster");
        InputStream stdIn = process.getInputStream();

        boolean messagesLeft = true;
        while (messagesLeft) {
            int character;
            String content = "";
            while ((character = stdIn.read()) != '\n') {
                content += (char) character;
            }
            LOGGER.info(content);
            if (content.startsWith("Sound patch level")) {
                messagesLeft = false;
            }
        }

        LOGGER.info("Done clearing initial messages from Filibuster");
    }

    private void testSynthesize() {
        SpeechUnit speechUnit = new SpeechUnit("Den bruna räven hoppade över den lata hunden.");
        setSpeechUnit(speechUnit);
        synthesize();
    }

    String[] getCommand(String logFileName) {
        String filibusterScript = filibusterHome.getHome() + "filibuster.tcl";
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

    @Override
    boolean isHealthy() {
        return process.isHealthy();
    }

    @Override
    void kill() {
        process.kill();
    }
}




/*

/home/folke/filibuster/Synthesis/SynthesisCore/filibuster.tcl -lang sv -mode batch -textfile - -outdir - -rate 22050 -log /var/log/mtm/speech-synthesis-server/filibuster-17-Jun-0003.log -debug 1 -print_header FILESIZE



*/



