package balbucio.org.firebase4j.exception;

import lombok.Getter;

/**
 * Este erro é lançado quando o token enviado é inválido.
 */
public class InvalidIdTokenException extends Exception{

    @Getter
    private String idToken;

    public InvalidIdTokenException(String message, String idToken) {
        super(message);
        this.idToken = idToken;
    }
}
