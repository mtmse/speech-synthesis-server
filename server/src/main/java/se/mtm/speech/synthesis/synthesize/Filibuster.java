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

class Filibuster extends Synthesizer implements Runnable { // NOPMD
    private static final Logger LOGGER = LoggerFactory.getLogger(Filibuster.class);

    private final FilibusterPool pool;
    private final SpeechSynthesizer synthesizer;
    private final Timeout timeout;
    private FilibusterProcess process;
    private final FilibusterHome filibusterHome;
    private final LogHome logHome;

    private Filibuster(Builder builder) {
        this.process = builder.process;
        this.pool = builder.pool;
        this.synthesizer = builder.synthesizer;
        this.filibusterHome = builder.filibusterHome;
        this.logHome = builder.logHome;
        this.timeout = builder.timeout;
        this.timeToDie = System.currentTimeMillis() + builder.ttl.getTtlInMilliseconds();

        if (process == null) {
            createFilibusterProcess();
        }
    }

    @Override
    public void run() {
        try {
            SynthesizedSound synthesised = synthesize();
            synthesizer.addSynthesizedParagraph(synthesised);
            pool.returnFilibuster(this);
        } catch (FilibusterException e) {
            LOGGER.warn(e.getMessage());
            kill();
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
        return LogName.getLogFileName(logHome);
    }

    private SynthesizedSound synthesize() {
        String key = getSpeechUnitKey();
        String speechUnitText = getSpeechUnitText();
        // todo debug logging
        String msg = "Synthesising '" + speechUnitText + "' that is found using the key '" + key + "'";
        LOGGER.info(msg);
        process.write(speechUnitText);
        byte[] sound = process.getSound();

        // todo debug logging
        LOGGER.info("Done synthesising");

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
        String text = "The quick brown fox jumps over the lazy dog";
        // todo debug logging
        String msg = "Test synthesize '" + text + "'";
        LOGGER.info(msg);
        SpeechUnit speechUnit = new SpeechUnit(text);
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

    @Override
    public String getType() {
        return "Filibuster";
    }

    public static class Builder {
        private FilibusterProcess process;
        private FilibusterPool pool; // NOPMD
        private SpeechSynthesizer synthesizer; // NOPMD
        private FilibusterHome filibusterHome = new FilibusterHome("."); // NOPMD
        private LogHome logHome = new LogHome("."); // NOPMD
        private Timeout timeout; // NOPMD
        private TimeToLive ttl = new TimeToLive(0); // NOPMD

        public Builder fakeProcess(FilibusterProcess process) {
            this.process = process;
            return this;
        }

        public Builder pool(FilibusterPool pool) {
            this.pool = pool;
            return this;
        }

        public Builder synthesizer(SpeechSynthesizer synthesizer) {
            this.synthesizer = synthesizer;
            return this;
        }

        public Builder filibusterHome(FilibusterHome filibusterHome) {
            this.filibusterHome = filibusterHome;
            return this;
        }

        public Builder logHome(LogHome logHome) {
            this.logHome = logHome;
            return this;
        }

        public Builder timeout(Timeout timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder ttl(TimeToLive ttl) {
            this.ttl = ttl;
            return this;
        }

        public Filibuster build() {
            return new Filibuster(this); // NOPMD
        }
    }
}
