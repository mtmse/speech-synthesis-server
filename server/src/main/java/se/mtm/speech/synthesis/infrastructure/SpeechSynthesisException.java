package se.mtm.speech.synthesis.infrastructure;

public class SpeechSynthesisException extends RuntimeException {
    public SpeechSynthesisException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
