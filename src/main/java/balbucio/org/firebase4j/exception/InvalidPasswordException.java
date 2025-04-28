package balbucio.org.firebase4j.exception;

import lombok.Getter;

public class InvalidPasswordException extends Exception {

    @Getter
    private final String password;

    public InvalidPasswordException(String msg, String password) {
        super(msg);
        this.password = password;
    }
}
