package se.mtm.speech.synthesis.synyhesize;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

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

     // todo create new Filibuster if the pool can fit one more
     // todo add an algorithm to increase the pool with one instance at the time until the resources on the host are limited

}
