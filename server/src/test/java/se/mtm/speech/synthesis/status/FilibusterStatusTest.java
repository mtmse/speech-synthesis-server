package se.mtm.speech.synthesis.status;

import org.junit.Test;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FilibusterStatusTest {
    @Test
    public void get_type() {
        String expected = "test type";
        FilibusterStatus filibusterStatus = new FilibusterStatus.Builder()
                .type("test type")
                .build();

        String actual = filibusterStatus.getType();

        assertThat(actual, is(expected));
    }

    @Test
    public void get_created() {
        String expected = "1970-01-01 01:00:00";
        FilibusterStatus filibusterStatus = new FilibusterStatus.Builder()
                .created(new Date(0))
                .build();

        String actual = filibusterStatus.getCreated();

        assertThat(actual, is(expected));
    }

    @Test
    public void get_end_of_life() {
        String expected = "1970-01-01 01:00:00";
        FilibusterStatus filibusterStatus = new FilibusterStatus.Builder()
                .endOfLife(new Date(0))
                .build();

        String actual = filibusterStatus.getEndOfLife();

        assertThat(actual, is(expected));
    }

}
