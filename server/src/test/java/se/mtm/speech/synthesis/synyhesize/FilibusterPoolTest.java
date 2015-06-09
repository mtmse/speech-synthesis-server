package se.mtm.speech.synthesis.synyhesize;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static junit.framework.Assert.assertNotSame;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class FilibusterPoolTest {
    @Test
    public void accept_young_filibusters() {
        int maxPoolSize = 0;
        int timeToLive = Integer.MAX_VALUE;

        FilibusterPool pool = new FilibusterPool(maxPoolSize, timeToLive);
        assertFalse("No Filibuster should be available", pool.peekFilibuster());

        Filibuster filibuster = new Filibuster(pool, timeToLive);

        pool.returnFilibuster(filibuster);

        assertTrue("One Filibuster should be available", pool.peekFilibuster());
    }

    @Test
    public void do_not_accept_too_old_filibusters() {
        int maxPoolSize = 0;
        int timeToLive = Integer.MIN_VALUE;

        FilibusterPool pool = new FilibusterPool(maxPoolSize, timeToLive);
        assertFalse("No Filibuster should be available", pool.peekFilibuster());

        Filibuster filibuster = new Filibuster(pool, timeToLive);

        pool.returnFilibuster(filibuster);

        assertFalse("No Filibuster should be available, the one that was offered was too old", pool.peekFilibuster());
    }

    @Test
    public void invalidate_all_filibusters() {
        FilibusterPool fakePool = mock(FilibusterPool.class);
        Queue<Filibuster> waiting = new LinkedBlockingQueue<>();
        List<Filibuster> all = new LinkedList<>();

        Filibuster idle = new Filibuster(fakePool, Integer.MAX_VALUE);
        waiting.offer(idle);
        all.add(idle);

        Filibuster running = new Filibuster(fakePool, Integer.MAX_VALUE);
        all.add(running);

        FilibusterPool pool = new FilibusterPool(waiting, all, 2);

        pool.invalidate();

        assertThat(waiting.size(), is(1));
        assertThat(all.size(), is(2));

        assertTrue("The running Filibuster shall not be used after this execution", running.isTooOld());

        Filibuster newIdle = pool.getFilibuster();
        assertNotSame("The waiting Filibuster should have been recreated, but found the old one", idle, newIdle);
    }


    // todo create new Filibuster if the pool can fit one more
    // todo add an algorithm to increase the pool with one instance at the time until the resources on the host are limited

}
