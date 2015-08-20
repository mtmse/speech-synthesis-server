package se.mtm.speech.synthesis.synthesize;

import org.junit.Test;
import se.mtm.speech.synthesis.infrastructure.configuration.Timeout;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FilibusterPoolTest {
    private static final String FILIBUSTER_HOME = "not used";
    private static final String LOG_HOME = "not used";

    @Test
    public void accept_young_filibusters() {
        int maxPoolSize = 0;
        int timeToLive = Integer.MAX_VALUE;

        FilibusterPool pool = new FilibusterPool(maxPoolSize, timeToLive);
        assertFalse("No Filibuster should be available", pool.peekFilibuster());

        FilibusterProcess process = mock(FilibusterProcess.class);
        Filibuster filibuster = new Filibuster(process, pool, null, FILIBUSTER_HOME, LOG_HOME, new Timeout(0), 1000);

        pool.returnFilibuster(filibuster);

        assertTrue("One Filibuster should be available", pool.peekFilibuster());
    }

    @Test
    public void do_not_accept_too_old_filibusters() {
        int maxPoolSize = 0;
        int timeToLive = Integer.MIN_VALUE;

        FilibusterPool pool = new FilibusterPool(maxPoolSize, timeToLive);
        assertFalse("No Filibuster should be available", pool.peekFilibuster());

        FilibusterProcess process = mock(FilibusterProcess.class);
        Filibuster filibuster = new Filibuster(process, pool, null, FILIBUSTER_HOME, LOG_HOME, new Timeout(0), timeToLive);

        pool.returnFilibuster(filibuster);

        assertFalse("No Filibuster should be available, the one that was offered was too old", pool.peekFilibuster());
        verify(process).kill();
    }

    @Test
    public void invalidate_all_filibusters() throws Exception {
        FilibusterPool fakePool = mock(FilibusterPool.class);
        Queue<Synthesizer> waiting = new LinkedBlockingQueue<>();
        Queue<Synthesizer> all = new LinkedList<>();

        FilibusterProcess idleProcess = mock(FilibusterProcess.class);
        Synthesizer idle = new Filibuster(idleProcess, fakePool, null, FILIBUSTER_HOME, LOG_HOME, new Timeout(0), Integer.MAX_VALUE);
        waiting.offer(idle);
        all.add(idle);

        FilibusterProcess runningProcess = mock(FilibusterProcess.class);
        Filibuster running = new Filibuster(runningProcess, fakePool, null, FILIBUSTER_HOME, LOG_HOME, new Timeout(0), Integer.MAX_VALUE);
        all.add(running);

        FilibusterPool pool = new FilibusterPool(waiting, all, 2);

        pool.invalidate();

        assertThat(waiting.size(), is(1));
        assertThat(all.size(), is(2));

        assertTrue("The running Filibuster shall not be used after this execution", running.isTooOld());

        Synthesizer newIdle = pool.getSynthesizer();
        assertNotSame("The waiting Filibuster should have been recreated, but found the old one", idle, newIdle);

        verify(idleProcess).kill();
    }
}
