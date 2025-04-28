package balbucio.org.firebase4j.exception;

import lombok.Getter;

public class PhoneNumberExistsException extends Exception {

    @Getter
    private final String phoneNumber;

    public PhoneNumberExistsException(String message, String phoneNumber) {
        super(message);
        this.phoneNumber = phoneNumber;
    }
}
