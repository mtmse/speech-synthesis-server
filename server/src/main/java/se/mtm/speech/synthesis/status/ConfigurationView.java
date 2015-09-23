package se.mtm.speech.synthesis.status;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

public class ConfigurationView extends CommonView {
    public ConfigurationView() {
        super("configuration.mustache", Charset.forName("UTF-8"));
    }

    public String getConfiguration() throws IOException {
        String configurationFile = "/etc/opt/speech-synthesis-server/configuration.yaml";
        InputStream releaseNotes = getClass().getResourceAsStream(configurationFile);

        if (releaseNotes == null) {
            return "No configuration found, searched for " + configurationFile;
        }

        List<String> lines = IOUtils.readLines(releaseNotes);
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : lines) {
            stringBuilder.append(line);
            stringBuilder.append('\n');
        }

        return stringBuilder.toString();
    }
}
