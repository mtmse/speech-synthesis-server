package se.mtm.speech.synthesis.status;

import io.dropwizard.views.View;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StatusView extends View {

    public StatusView() {
        super("status.mustache", Charset.forName("UTF-8"));
    }

    public String getGenerationDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // NOPMD
        return sdf.format(new Date());
    }
}
