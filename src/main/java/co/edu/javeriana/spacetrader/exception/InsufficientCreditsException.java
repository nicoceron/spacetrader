package co.edu.javeriana.spacetrader.exception;

public class InsufficientCreditsException extends RuntimeException {
    public InsufficientCreditsException(String message) {
        super(message);
    }
}