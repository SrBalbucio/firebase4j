package balbucio.org.firebase4j.exception;

import balbucio.org.firebase4j.model.User;
import lombok.Getter;

/**
 * Este erro é lançado quando o usuário enviado não é encontrado.
 */
public class UserNotFoundException extends Exception{

    @Getter
    private String idToken;

    public UserNotFoundException(String message, String idToken) {
        super(message);
        this.idToken = idToken;
    }
}
