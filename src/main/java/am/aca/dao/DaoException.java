package am.aca.dao;

/**
 * Created by Vardan on 02.06.2017
 */
public class DaoException extends RuntimeException {

    public DaoException() {}

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}