package se.mtm.speech.synthesis.status;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

public class AboutView extends CommonView {
    public AboutView() {
        super("about.mustache", Charset.forName("UTF-8"));
    }

    public String getReleaseNotes() throws IOException {
        InputStream releaseNotes = getClass().getResourceAsStream("/release_notes.txt");

        List<String> lines = IOUtils.readLines(releaseNotes);
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : lines) {
            stringBuilder.append(line);
            stringBuilder.append('\n');
        }

        return stringBuilder.toString();
    }
}
