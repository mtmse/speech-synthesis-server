package se.mtm.speech.synthesis.status;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AboutViewTest {
    @Test
    public void get_release_notes() throws Exception {
        AboutView view = new AboutView();

        String actual = view.getReleaseNotes();

        assertTrue("The release notes should not be empty", actual.length() > 10);
    }
}
