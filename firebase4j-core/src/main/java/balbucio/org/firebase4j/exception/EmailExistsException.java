package balbucio.org.firebase4j.exception;

import lombok.Getter;

/**
 * Este erro é lançado quando o email que o usuário está tentando cadastrar já existe no banco de dados.
 */
public class EmailExistsException extends Exception{

    @Getter
    private final String email;

    public EmailExistsException(String message, String email) {
        super(message);
        this.email = email;
    }
}
