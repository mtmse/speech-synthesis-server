package se.mtm.speech.synthesis.status;

import se.mtm.speech.synthesis.synthesize.SpeechSynthesizer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/")
public class StatusResource {
    private final SpeechSynthesizer speechSynthesizer;

    public StatusResource(SpeechSynthesizer speechSynthesizer) {
        this.speechSynthesizer = speechSynthesizer;
    }

    @GET
    @Produces("text/html;charset=UTF-8")
    public StatusView showSpeechServerStatus() {
        return new StatusView(speechSynthesizer);
    }
}
