package am.aca.orgflix.dao;

import am.aca.orgflix.entity.User;

/**
 * Interface for user DAO
 */
public interface UserDAO {
    // Create

    /**
     * Add new user to DB
     *
     * @param user the user object to be added to DB
     * @return id of added new user in DB
     */
    int add(User user);

    //Read

    /**
     * Return user by user Id
     *
     * @param id the id of the user in DB
     * @return user object with the matched id
     */
    User get(int id);

    /**
     * Return user by user user's email
     *
     * @param email the email of the user in DB
     * @return user object with the matched id
     */
    User get(String email);

    /**
     * Return user by user user's nickname
     *
     * @param nick the nickname of the user in DB
     * @return user object with the matched id
     */
    User getByNick(String nick);

    /**
     * Return user authenticated by user's email and password
     *
     * @param email the email of the user in DB
     * @param pass  the password of the user in DB
     * @return user object with the matched details
     */
    User authenticate(String email, String pass);

    //Update

    /**
     * Update the user's data with selected id in DB based on the provided fields' data
     *
     * @param id    user id to search for
     * @param nick  user's nick name to be updated to
     * @param name  user's name to be updated to (not required)
     * @param pass  user's password to be updated to
     * @param email user's email to be updated to
     * @return true if update was successful, otherwise false
     */
    boolean edit(int id, String nick, String name, String pass, String email);

    /**
     * @param user the user whose data should be updated according to its current field values
     * @return true if update was successful, otherwise false
     * @see am/aca/dao/jdbc/impljdbc/JdbcUserDAO.java:131
     * <p>
     * Update the provided user's data in DB
     */
    boolean edit(User user);

    /**
     * @param id    user id to search for
     * @param nick  user's nick name to be updated to
     * @param pass  user's password to be updated to
     * @param email user's email to be updated to
     * @return true if update was successful, otherwise false
     * @see am/aca/dao/jdbc/impljdbc/JdbcUserDAO.java:131
     * <p>
     * Update the user's data with selected id in DB based on the provided fields' data
     */
    boolean edit(int id, String nick, String pass, String email);

    //Delete

    /**
     * Removed user with provided id from DB
     *
     * @param id of the user to be removed
     * @return true if the remove was successful, otherwise false
     */
    boolean remove(int id);

    /**
     * @param user the user to be removed
     * @return true if the remove was successful, otherwise false
     * @see am/aca/dao/jdbc/impljdbc/JdbcUserDAO.java:178
     * <p>
     * Removed provided user from DB
     */
    boolean remove(User user);

}
