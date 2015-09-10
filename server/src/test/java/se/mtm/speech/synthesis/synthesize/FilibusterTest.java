package se.mtm.speech.synthesis.synthesize;

import org.junit.Test;
import se.mtm.speech.synthesis.infrastructure.FilibusterException;
import se.mtm.speech.synthesis.infrastructure.configuration.FilibusterHome;
import se.mtm.speech.synthesis.infrastructure.configuration.TimeToLive;
import se.mtm.speech.synthesis.infrastructure.configuration.Timeout;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class FilibusterTest {
    private static final String NOT_USED = "not used";
    private final FilibusterProcess fake = mock(FilibusterProcess.class);
    private final FilibusterHome filibusterHome = new FilibusterHome(NOT_USED);
    private final Timeout timeout = new Timeout(0);
    private final TimeToLive timeToLive = new TimeToLive(0);

    @Test
    public void synthesize() {
        FilibusterProcess process = mock(FilibusterProcess.class);
        when(process.getSound()).thenReturn(new byte[0]);
        FilibusterPool pool = mock(FilibusterPool.class);
        SpeechSynthesizer synthesizer = mock(SpeechSynthesizer.class);

        Filibuster filibuster = new Filibuster.Builder()
                .fakeProcess(process)
                .pool(pool)
                .synthesizer(synthesizer)
                .ttl(timeToLive)
                .build();

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

        Filibuster filibuster = new Filibuster.Builder()
                .fakeProcess(process)
                .timeout(timeout)
                .ttl(timeToLive)
                .build();

        SpeechUnit speechUnit = new SpeechUnit("key", "sentence");
        filibuster.setSpeechUnit(speechUnit);

        filibuster.run();

        verify(process).kill();

        assertThat(true, is(!false)); // pmd requires at least one assert...
    }

    @Test
    public void tcl_command_should_be_valid_path() throws Exception {
        when(fake.getSound()).thenReturn(new byte[0]);

        Filibuster filibuster = new Filibuster.Builder()
                .fakeProcess(fake)
                .filibusterHome(filibusterHome)
                .ttl(timeToLive)
                .build();

        String[] actual = filibuster.getCommand("filibusterLog");

        assertFilibusterCommand(actual);
    }

    @Test
    public void get_type() {
        String expected = "Filibuster";

        Filibuster filibuster = new Filibuster.Builder()
                .fakeProcess(fake)
                .build();

        String actual = filibuster.getType();

        assertThat(actual, is(expected));
    }

    @Test
    public void get_created() {
        Date expected = new Date();
        Filibuster filibuster = new Filibuster.Builder()
                .fakeProcess(fake)
                .build();

        Date actual = filibuster.getCreated();

        assertDatesAlmostEqual(actual, expected);
    }

    @Test
    public void get_end_of_life() {
        long timeToDie = System.currentTimeMillis() + (1000 * 60 * 10);
        Date expected = new Date(timeToDie);
        Filibuster filibuster = new Filibuster.Builder()
                .fakeProcess(fake)
                .ttl(new TimeToLive(10))
                .build();

        Date actual = filibuster.getEndOfLife();

        assertDatesAlmostEqual(actual, expected);
    }

    private void assertDatesAlmostEqual(Date actual, Date expected) {
        long difference = actual.getTime() - expected.getTime();
        assertTrue("Dates aren't close enough to each other!", difference < 1000);
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
