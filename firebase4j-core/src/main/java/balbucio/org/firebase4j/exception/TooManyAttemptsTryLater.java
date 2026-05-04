package balbucio.org.firebase4j.exception;

public class TooManyAttemptsTryLater extends RuntimeException {
    public TooManyAttemptsTryLater(String message) {
        super(message);
    }
}
