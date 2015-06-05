package se.mtm.speech.synthesis.status;

import io.dropwizard.views.View;

import java.nio.charset.Charset;

public class StatusView extends View {

    public StatusView() {
        super("status.mustache", Charset.forName("UTF-8"));
    }

    public String getGenerationDate() {
        return "2014-10-15 09:40:27";
    }
}
