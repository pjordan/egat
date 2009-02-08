package edu.umich.eecs.ai.egat.cli;

/**
 * @author Patrick Jordan
 */
public class CommandProcessingException extends RuntimeException {
    public CommandProcessingException() {
    }

    public CommandProcessingException(String message) {
        super(message);
    }

    public CommandProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandProcessingException(Throwable cause) {
        super(cause);
    }
}
