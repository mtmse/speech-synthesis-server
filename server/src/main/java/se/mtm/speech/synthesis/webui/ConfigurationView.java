package se.mtm.speech.synthesis.webui;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

public class ConfigurationView extends CommonView {
    public ConfigurationView() {
        super("configuration.mustache", Charset.forName("UTF-8"));
    }

    public List<String> getConfiguration() throws IOException {
        File configurationFile = new File("/etc/opt/speech-synthesis-server/configuration.yaml");
        List<String> lines = new LinkedList<>();

        try {
            lines = FileUtils.readLines(configurationFile);
        } catch (FileNotFoundException e) {
            lines.add(e.getMessage());
        }

        return lines;
    }
}
