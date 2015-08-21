package se.mtm.speech.synthesis.synthesize;

import org.junit.Test;
import se.mtm.speech.synthesis.infrastructure.FilibusterException;
import se.mtm.speech.synthesis.infrastructure.configuration.FilibusterHome;
import se.mtm.speech.synthesis.infrastructure.configuration.Timeout;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class FilibusterTest {

    public static final String NOT_USED = "not used";

    @Test
    public void synthesize() {
        FilibusterProcess process = mock(FilibusterProcess.class);
        when(process.getSound()).thenReturn(new byte[0]);
        FilibusterPool pool = mock(FilibusterPool.class);
        SpeechSynthesizer synthesizer = mock(SpeechSynthesizer.class);
        Timeout timeout = new Timeout(0);
        long timeToLive = 0;

        Filibuster filibuster = new Filibuster(process, pool, synthesizer, new FilibusterHome(NOT_USED), NOT_USED, timeout, timeToLive);

        SpeechUnit speechUnit = new SpeechUnit("key", "sentence");
        filibuster.setSpeechUnit(speechUnit);

        filibuster.run();

        verify(process).write("sentence");
        verify(process).getSound();
        verify(synthesizer).addSynthesizedParagraph(any(SynthesizedSound.class));
        verify(pool).returnFilibuster(filibuster);

        assertThat(true, is(!false)); // pmd requires at least one assert...
    }

    @Test
    public void synthesize_timeout() {
        FilibusterProcess process = mock(FilibusterProcess.class);
        when(process.getSound()).thenThrow(FilibusterException.class);
        FilibusterPool pool = mock(FilibusterPool.class);
        SpeechSynthesizer synthesizer = mock(SpeechSynthesizer.class);
        Timeout timeout = new Timeout(0);
        long timeToLive = 0;

        Filibuster filibuster = new Filibuster(process, pool, synthesizer, new FilibusterHome(NOT_USED), NOT_USED, timeout, timeToLive);

        SpeechUnit speechUnit = new SpeechUnit("key", "sentence");
        filibuster.setSpeechUnit(speechUnit);

        filibuster.run();

        verify(process).kill();

        assertThat(true, is(!false)); // pmd requires at least one assert...
    }

    @Test
    public void tcl_command_should_be_valid_path() throws Exception {
        FilibusterProcess process = mock(FilibusterProcess.class);
        when(process.getSound()).thenReturn(new byte[0]);
        FilibusterPool pool = mock(FilibusterPool.class);
        SpeechSynthesizer synthesizer = mock(SpeechSynthesizer.class);
        Timeout timeout = new Timeout(0);
        long timeToLive = 0;

        Filibuster filibuster = new Filibuster(process, pool, synthesizer, new FilibusterHome(NOT_USED), NOT_USED, timeout, timeToLive);

        String[] actual = filibuster.getCommand("filibusterLog");

        assertFilibusterCommand(actual);
    }

    private void assertFilibusterCommand(String... commandParts) throws IOException {
        String expected = "filibuster.tcl";
        String filibusterCommand = null;
        for (String part : commandParts) {
            if (part.contains(expected)) {
                filibusterCommand = part;
                break;
            }
        }
        assertNotNull("Expected to find a command to start Filibuster with", filibusterCommand);
        File command = new File(filibusterCommand);

        String actual = command.getName();

        assertThat(actual, is(expected));
    }

}
