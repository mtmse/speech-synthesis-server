package se.mtm.speech.synthesis.synyhesize;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DispatcherTest {

    @Test
    public void start_and_shut_down() throws Exception {
        SpeechSynthesizer speechSynthesizer = mock(SpeechSynthesizer.class);
        when(speechSynthesizer.peekNext()).thenReturn(false);
        int idleTime = 1;

        Dispatcher dispatcher = new Dispatcher(null, speechSynthesizer, idleTime);

        assertFalse("Dispatcher should not be running", dispatcher.isRunning());

        Thread thread = new Thread(dispatcher);
        thread.start();
        pause();

        assertTrue("Dispatcher should be running", dispatcher.isRunning());

        pause();

        dispatcher.shutDown();
        pause();

        assertFalse("Dispatcher should not be running", dispatcher.isRunning());
    }

    private void pause() throws InterruptedException {
        Thread.sleep(5);
    }
}
