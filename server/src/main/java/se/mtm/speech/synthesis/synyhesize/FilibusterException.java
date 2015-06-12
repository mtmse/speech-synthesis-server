package se.mtm.speech.synthesis.synyhesize;

class FilibusterException extends RuntimeException {
    public FilibusterException(String message) {
        super(message);
    }

    public FilibusterException(Throwable throwable) {
        super(throwable);
    }

    public FilibusterException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
