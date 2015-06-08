package se.mtm.speech.synthesis.status;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StatusPageTest {
    @Test
    public void parse_raw_date_string() throws Exception {
        String sample = "Page generated at 2014-10-15 09:40:27";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // NOPMD
        Date expected = sdf.parse("2014-10-15 09:40:27");

        StatusPage page = new StatusPage();

        Date actual = page.parseGenerationDate(sample);

        assertThat(actual, is(expected));
    }
}
