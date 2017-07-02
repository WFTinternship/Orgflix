package am.aca.orgflix.dao;

/**
 * DAO exception class for JDBC implementation
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