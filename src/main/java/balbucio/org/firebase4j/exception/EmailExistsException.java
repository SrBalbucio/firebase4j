package balbucio.org.firebase4j.exception;

import lombok.Getter;

public class EmailExistsException extends Exception{

    @Getter
    private String email;

    public EmailExistsException(String message, String email) {
        super(message);
        this.email = email;
    }
}
