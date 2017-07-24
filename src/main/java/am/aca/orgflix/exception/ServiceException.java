package am.aca.orgflix.exception;

/**
 * Service layer exception
 */
public class ServiceException extends RuntimeException {
    public ServiceException() {

    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
