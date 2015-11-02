package se.mtm.speech.synthesis.webui;

import io.dropwizard.views.View;
import se.mtm.speech.synthesis.infrastructure.Version;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class CommonView extends View {
    private final Version version;

    CommonView(String templateName, Charset charset) {
        super(templateName, charset);
        version = new Version();
    }

    public String getGenerationDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        return sdf.format(new Date());
    }

    String getVersion() {
        return version.getVersion();
    }

    String getBuildTime() {
        return version.getBuildTime();
    }
}
