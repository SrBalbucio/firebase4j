package balbucio.org.firebase4j.exception;

public class BadConfigurationException extends Exception{

    public BadConfigurationException(String message) {
        super("Your Firebase is misconfigured or not ready to receive calls: "+ message);
    }
}
