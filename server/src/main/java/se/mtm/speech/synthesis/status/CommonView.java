package se.mtm.speech.synthesis.status;

import com.jcabi.manifests.Manifests;
import io.dropwizard.views.View;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class CommonView extends View {
    CommonView(String templateName, Charset charset) {
        super(templateName, charset);
    }

    public String getGenerationDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        return sdf.format(new Date());
    }

    String getVersion() {
        String version;
        try {
            version = Manifests.read("Version");
        } catch (Exception e) {
            version = "version not found in jar file";
        }

        return version;
    }

    String getBuildTime() {
        String buildDate;
        try {
            buildDate = Manifests.read("Build-Date");
        } catch (Exception e) {
            buildDate = "build time not found in jar file";
        }

        return buildDate;
    }
}
