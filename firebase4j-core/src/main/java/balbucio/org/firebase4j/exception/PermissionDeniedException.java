package balbucio.org.firebase4j.exception;

public class PermissionDeniedException extends Exception{

    public PermissionDeniedException(int code) {
        super("Permission denied: " + code);
    }
}
