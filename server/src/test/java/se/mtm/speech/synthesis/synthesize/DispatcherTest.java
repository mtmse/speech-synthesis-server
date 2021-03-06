package se.mtm.speech.synthesis.synthesize;

import org.junit.Test;
import se.mtm.speech.synthesis.infrastructure.configuration.IdleTime;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DispatcherTest {

    @Test
    public void start_and_shut_down() throws Exception {
        SpeechSynthesizer speechSynthesizer = mock(SpeechSynthesizer.class);
        when(speechSynthesizer.peekNext()).thenReturn(false);
        IdleTime idleTime = new IdleTime(1);

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

    @Test
    public void invalidate_filibusters(){ // NOPMD
        FilibusterPool pool = mock(FilibusterPool.class);
        Dispatcher dispatcher = new Dispatcher(pool, null, new IdleTime(0));

        dispatcher.invalidate();

        verify(pool).invalidate();
    }

    private void pause() throws InterruptedException {
        Thread.sleep(5);
    }
}
