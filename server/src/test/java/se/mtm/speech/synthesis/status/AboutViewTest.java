package se.mtm.speech.synthesis.status;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;

public class AboutViewTest {
    @Test
    public void get_release_notes() throws Exception {
        AboutView view = new AboutView();

        List<String> actual = view.getReleaseNotes();

        assertFalse("The release notes should not be empty", actual.isEmpty());
    }
}
