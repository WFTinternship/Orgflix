package am.aca.orgflix.service;

import am.aca.orgflix.entity.User;

import java.util.List;

/**
 * Interface for user service layer
 */
public interface UserService {

    /**
     * Creates the given user
     *
     * @param user the User object to be created
     * @return the id of the created user, -1 if operation failed
     */

    int add(User user);

    /**
     * Retrieves the user having the given ID
     *
     * @param id the ID of the desired user
     * @return the User object having the given ID
     */
    User getById(int id);

    /**
     * Retrieves all users
     *
     * @return list of all User objects registered in DB
     */
    List<User> getAll();

    /**
     * Retrieves the User object passed the authentication by email and password
     *
     * @param email email to pass authentication
     * @param pass  password to pass authentication
     * @return the User who passed the authentication
     */
    User authenticate(String email, String pass);

    /**
     * Retrieves the User object having the given email
     *
     * @param email the email to filter the user by
     * @return the User object with the given email
     */
    User getByEmail(String email);

    /**
     * Retrieves the User object having the given nickname
     *
     * @param nick the email to filter the user by
     * @return the User object with the given email
     */
    User getByNick(String nick);

    /**
     * Updates the current user to the given user
     *
     * @param user the object to update the current user to
     * @return true if the operation was performed successfully, false otherwise
     */
    boolean edit(User user);

}
