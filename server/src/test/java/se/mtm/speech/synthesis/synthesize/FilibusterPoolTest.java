package se.mtm.speech.synthesis.synthesize;

import org.junit.Test;
import se.mtm.speech.synthesis.infrastructure.configuration.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FilibusterPoolTest {

    @Test
    public void accept_young_filibusters() {
        MaxFilibusters maxPoolSize = new MaxFilibusters(0);

        FilibusterPool pool = new FilibusterPool.Builder()
                .maxPoolSize(maxPoolSize)
                .minimumMemory(new MinimumMemory(2))
                .build();

        assertFalse("No Filibuster should be available", pool.peekFilibuster());

        FilibusterProcess process = mock(FilibusterProcess.class);
        when(process.isHealthy()).thenReturn(true);

        Filibuster filibuster = new Filibuster.Builder()
                .fakeProcess(process)
                .ttl(new TimeToLive(1))
                .build();

        pool.returnFilibuster(filibuster);

        assertTrue("One Filibuster should be available", pool.peekFilibuster());
    }

    @Test
    public void do_not_accept_too_old_filibusters() {
        MaxFilibusters maxPoolSize = new MaxFilibusters(0);
        TimeToLive timeToLive = new TimeToLive(Integer.MIN_VALUE);

        FilibusterPool pool = new FilibusterPool.Builder()
                .maxPoolSize(maxPoolSize)
                .minimumMemory(new MinimumMemory(2))
                .build();

        assertFalse("No Filibuster should be available", pool.peekFilibuster());

        FilibusterProcess process = mock(FilibusterProcess.class);
        Filibuster filibuster = new Filibuster.Builder()
                .fakeProcess(process)
                .ttl(timeToLive)
                .build();

        pool.returnFilibuster(filibuster);

        assertFalse("No Filibuster should be available, the one that was offered was too old", pool.peekFilibuster());
        verify(process).kill();
    }

    @Test
    public void invalidate_all_filibusters() throws Exception {
        Queue<Synthesizer> waiting = new LinkedBlockingQueue<>();
        Queue<Synthesizer> all = new LinkedList<>();

        FilibusterPool pool = new FilibusterPool.Builder()
                .maxPoolSize(new MaxFilibusters(2))
                .waiting(waiting)
                .all(all)
                .build();

        FilibusterProcess idleProcess = mock(FilibusterProcess.class);
        Synthesizer idle = addIdleProcess(waiting, all, idleProcess);

        Filibuster running = addRunningProcess(all);

        pool.invalidate();

        assertThat(waiting.size(), is(1));
        assertThat(all.size(), is(2));

        assertTrue("The running Filibuster shall not be used after this execution", running.isTooOld());

        Synthesizer newIdle = pool.getSynthesizer();
        assertNotSame("The waiting Filibuster should have been recreated, but found the old one", idle, newIdle);

        verify(idleProcess).kill();
    }

    private Synthesizer addIdleProcess(Queue<Synthesizer> waiting, Queue<Synthesizer> all, FilibusterProcess idleProcess) {
        Filibuster idle = new Filibuster.Builder()
                .fakeProcess(idleProcess)
                .ttl(new TimeToLive(Integer.MAX_VALUE))
                .build();

        waiting.offer(idle);
        all.add(idle);

        return idle;
    }

    private Filibuster addRunningProcess(Queue<Synthesizer> all) {
        FilibusterProcess runningProcess = mock(FilibusterProcess.class);
        Filibuster running = new Filibuster.Builder()
                .fakeProcess(runningProcess)
                .ttl(new TimeToLive(Integer.MAX_VALUE))
                .build();

        all.add(running);

        return running;
    }

    @Test
    public void get_a_healthy_filibuster_from_a_pool_of_unhealthy_filibusters() {
        Queue<Synthesizer> waiting = new LinkedBlockingQueue<>();
        Queue<Synthesizer> all = new LinkedList<>();

        FilibusterPool pool = new FilibusterPool.Builder()
                .maxPoolSize(new MaxFilibusters(2))
                .waiting(waiting)
                .all(all)
                .build();

        Synthesizer synthesizer = mock(Synthesizer.class);
        when(synthesizer.isHealthy()).thenReturn(false);
        waiting.add(synthesizer);
        all.add(synthesizer);

        Synthesizer actual = pool.getSynthesizer();

        assertTrue("Expected a healthy filibuster", actual.isHealthy());
    }

    @Test
    public void get_a_healthy_filibuster_from_an_empty_a_pool() {
        Queue<Synthesizer> waiting = new LinkedBlockingQueue<>();
        Queue<Synthesizer> all = new LinkedList<>();
        FilibusterPool pool = new FilibusterPool.Builder()
                .maxPoolSize(new MaxFilibusters(2))
                .waiting(waiting)
                .all(all)
                .build();

        Synthesizer actual = pool.getSynthesizer();

        assertTrue("Expected a healthy filibuster", actual.isHealthy());
    }
}
