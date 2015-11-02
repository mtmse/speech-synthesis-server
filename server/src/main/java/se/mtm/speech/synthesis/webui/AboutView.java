package se.mtm.speech.synthesis.webui;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

public class AboutView extends CommonView {
    public AboutView() {
        super("about.mustache", Charset.forName("UTF-8"));
    }

    public List<String> getReleaseNotes() throws IOException {
        InputStream releaseNotes = getClass().getResourceAsStream("/release_notes.txt");

        return IOUtils.readLines(releaseNotes);
    }
}
