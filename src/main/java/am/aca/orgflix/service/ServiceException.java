package am.aca.orgflix.service;

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
