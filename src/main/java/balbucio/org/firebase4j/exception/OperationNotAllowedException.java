package balbucio.org.firebase4j.exception;

/**
 * <p>Este erro é lançado quando alguma operação não é permitida.
 * Muitas vezes este erro pode significar má configuração do backend.
 * </p>
 */
public class OperationNotAllowedException extends Exception {

    public OperationNotAllowedException(String message) {
        super(message);
    }
}
