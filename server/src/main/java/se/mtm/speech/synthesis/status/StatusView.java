package se.mtm.speech.synthesis.status;

import se.mtm.speech.synthesis.synthesize.SpeechSynthesizer;
import se.mtm.speech.synthesis.synthesize.Synthesizer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class StatusView extends CommonView {
    private final SpeechSynthesizer speechSynthesizer;

    public StatusView(SpeechSynthesizer speechSynthesizer) {
        super("status.mustache", Charset.forName("UTF-8"));
        this.speechSynthesizer = speechSynthesizer;
    }

    public List<FilibusterStatus> getFilibusters() throws IOException {
        List<FilibusterStatus> filibusters = new LinkedList<>();

        for (Synthesizer synthesizer : speechSynthesizer.getSynthesizers()) {
            String type = synthesizer.getType();
            Date created = synthesizer.getCreated();
            Date endOfLife = synthesizer.getEndOfLife();

            FilibusterStatus status = new FilibusterStatus.Builder()
                    .type(type)
                    .created(created)
                    .endOfLife(endOfLife)
                    .build();

            filibusters.add(status);
        }

        return filibusters;
    }

}
