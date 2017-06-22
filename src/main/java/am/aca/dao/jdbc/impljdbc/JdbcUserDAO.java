package am.aca.dao.jdbc.impljdbc;

import am.aca.dao.DaoException;
import am.aca.dao.jdbc.BaseDAO;
import am.aca.dao.jdbc.UserDAO;
import am.aca.dao.jdbc.impljdbc.mapper.UserRowMapper;
import am.aca.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * DAO layer for User entity
 *
 * Created by David on 6/20/2017
 */

@Repository
public class JdbcUserDAO extends BaseDAO implements UserDAO {

    @Autowired
    public JdbcUserDAO(DataSource dataSource) {
        super(JdbcUserDAO.class);
        this.setDataSource(dataSource);
    }

    // CREATE

    /**
     * Add new user to DB
     *
     * @param nick  new user's nick name
     * @param name  new user's name (not required)
     * @param pass  new user's password
     * @param email new user's email
     * @return id of added new user in DB
     */
    @Override
    public int add(String nick, String name, String pass, String email) {
        // default return value which means nothing added
        int id = -1;
        // ensure that no null record will be passed for NOT NULL fields
        if (!checkRequiredFields(nick, pass, email))
            throw new DaoException("Nick, password and Email are required!");

        final String query = "INSERT INTO users (Nick,User_Name,Email,User_Pass) " +
                " VALUES( ? , ? , ? , ? ) ";

        KeyHolder holder = new GeneratedKeyHolder();

        int result = getJdbcTemplate().update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, nick);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, pass);
            return ps;
        }, holder);

        if (result == 1)
            id = holder.getKey().intValue();
        return id;
    }

    /**
     * @see am/aca/dao/jdbc/impljdbc/JdbcUserDAO.java:42
     *
     * Add new user to DB
     *
     * @param user user object to be added to DB
     * @return id of added new user in DB
     */
    @Override
    public int add(User user) {
        return add(user.getNick(), user.getUserName(), user.getPass(), user.getEmail());
    }

    /**
     * @see am/aca/dao/jdbc/impljdbc/JdbcUserDAO.java:42
     *
     * Add new user to DB
     *
     * @param nick  new user's nick name
     * @param pass  new user's password
     * @param email new user's email
     * @return id of added new user in DB
     */
    @Override
    public int add(String nick, String pass, String email) {
        return add(nick, "", pass, email);
    }

    // READ

    /**
     * Return user by user Id
     *
     * @param id the id of the user in DB
     * @return user object with the matched id
     */
    @Override
    public User get(int id) {
        final String query = "SELECT * FROM users WHERE ID = ? LIMIT 1";
        return (User) getJdbcTemplate().queryForObject(query, new Object[]{id}, new UserRowMapper());
    }

    /**
     * Return user by user user's email
     *
     * @param email the email of the user in DB
     * @return user object with the matched id
     */
    @Override
    public User get(String email) {
        final String query = "SELECT * FROM users WHERE Email = ? LIMIT 1";
        return (User) getJdbcTemplate().queryForObject(query, new Object[]{email}, new UserRowMapper());
    }

    //UPDATE

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
    @Override
    public boolean edit(int id, String nick, String name, String pass, String email) {
        // ensure that no null record will be passed for NOT NULL fields
        if (!checkRequiredFields(nick, pass, email)) return false;

        final String query = "UPDATE users SET Nick = ?,User_Name = ?,Email = ?, User_Pass = ? " +
                " WHERE ID = ? ";
        return getJdbcTemplate().update(query, nick, name, email, pass, id) == 1;
    }

    /**
     * @see am/aca/dao/jdbc/impljdbc/JdbcUserDAO.java:131
     *
     * Update the provided user's data in DB
     *
     * @param user the user whos data should be updated according to its current field values
     * @return true if update was successful, otherwise false
     */
    @Override
    public boolean edit(User user) {
        return edit(user.getId(), user.getNick(), user.getUserName(), user.getPass(), user.getEmail());
    }

    /**
     * @see am/aca/dao/jdbc/impljdbc/JdbcUserDAO.java:131
     *
     * Update the user's data with selected id in DB based on the provided fields' data
     *
     * @param id    user id to search for
     * @param nick  user's nick name to be updated to
     * @param pass  user's password to be updated to
     * @param email user's email to be updated to
     * @return true if update was successful, otherwise false
     */
    @Override
    public boolean edit(int id, String nick, String pass, String email) {
        return edit(id, nick, "", pass, email);
    }

    // DELETE

    /**
     * Removed user with provided id from DB
     *
     * @param id of the user to be removed
     * @return true if the remove was successful, otherwise false
     */
    @Override
    public boolean remove(int id) {
        final String query = "DELETE FROM users WHERE ID = ? ";
        return getJdbcTemplate().update(query, id) == 1;
    }

    /**
     * @see am/aca/dao/jdbc/impljdbc/JdbcUserDAO.java:178
     *
     * Removed provided user from DB
     *
     * @param user the user to be removed
     * @return true if the remove was successful, otherwise false
     */
    @Override
    public boolean remove(User user) {
        return remove(user.getId());
    }
}