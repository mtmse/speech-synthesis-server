package se.mtm.speech.synthesis.status;

import java.nio.charset.Charset;

public class StatusView extends CommonView {

    public StatusView() {
        super("status.mustache", Charset.forName("UTF-8"));
    }

}
