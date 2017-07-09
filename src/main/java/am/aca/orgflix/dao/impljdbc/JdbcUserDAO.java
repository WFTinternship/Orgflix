package am.aca.orgflix.dao.impljdbc;

import am.aca.orgflix.dao.BaseDAO;
import am.aca.orgflix.dao.DaoException;
import am.aca.orgflix.dao.UserDAO;
import am.aca.orgflix.dao.impljdbc.mapper.UserRowMapper;
import am.aca.orgflix.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * DAO layer for User entity
 */

@Component
public class JdbcUserDAO extends BaseDAO implements UserDAO {

    @Autowired
    public JdbcUserDAO(DataSource dataSource) {
        super(JdbcUserDAO.class);
        this.setDataSource(dataSource);
    }

    // CREATE

    /**
     * @see UserDAO#add(am.aca.orgflix.entity.User)
     */
    @Override
    public int add(User user) {
        // default return value which means nothing added
        int id = -1;
        // ensure that no null record will be passed for NOT NULL fields
        if (!checkRequiredFields(user.getNick(), user.getPass(), user.getEmail()))
            throw new DaoException("Nick, password and Email are required!");

        final String query = "INSERT INTO users (Nick,User_Name,Email,User_Pass) " +
                " VALUES( ? , ? , ? , ? ) ";

        KeyHolder holder = new GeneratedKeyHolder();

        int result = getJdbcTemplate().update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getNick());
            ps.setString(2, user.getUserName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPass());
            return ps;
        }, holder);

        if (result == 1) {
            id = holder.getKey().intValue();
            user.setId(id);
        }
        return id;
    }

    // READ

    /**
     * @see UserDAO#get(int)
     */
    @Override
    public User get(int id) {
        final String query = "SELECT * FROM users WHERE ID = ? LIMIT 1";
        return getJdbcTemplate().queryForObject(query, new Object[]{id}, new UserRowMapper());
    }

    /**
     * @see UserDAO#get(java.lang.String)
     */
    @Override
    public User get(String email) {
        final String query = "SELECT * FROM users WHERE Email = ? LIMIT 1";
        return getJdbcTemplate().queryForObject(query, new Object[]{email}, new UserRowMapper());
    }

    /**
     * @see UserDAO#getByNick(String)
     */
    @Override
    public User getByNick(String nick) {
        final String query = "SELECT * FROM users WHERE Nick = ? LIMIT 1";
        return getJdbcTemplate().queryForObject(query, new Object[]{nick}, new UserRowMapper());
    }

    /**
     * @see UserDAO#authenticate(java.lang.String, java.lang.String)
     */
    @Override
    public User authenticate(String email, String pass) {
        final String query = "SELECT * FROM users WHERE Email = ? AND User_Pass = ? LIMIT 1";
        Object obj = getJdbcTemplate().queryForObject(query, new Object[]{email, pass}, new UserRowMapper());
        return (User) obj;
    }

    //UPDATE

    /**
     * @see UserDAO#edit(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public boolean edit(int id, String nick, String name, String pass, String email) {
        // ensure that no null record will be passed for NOT NULL fields
        if (!checkRequiredFields(nick, pass, email))
            throw new DaoException("Illegal argument");

        final String query = "UPDATE users SET Nick = ?,User_Name = ?,Email = ?, User_Pass = ? " +
                " WHERE ID = ? ";
        return getJdbcTemplate().update(query, nick, name, email, pass, id) == 1;
    }

    /**
     * @see UserDAO#edit(am.aca.orgflix.entity.User)
     */
    @Override
    public boolean edit(User user) {
        return edit(user.getId(), user.getNick(), user.getUserName(), user.getPass(), user.getEmail());
    }

    /**
     * @see UserDAO#edit(int, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public boolean edit(int id, String nick, String pass, String email) {
        return edit(id, nick, "", pass, email);
    }

    // DELETE

    /**
     * @see UserDAO#remove(int)
     */
    @Override
    public boolean remove(int id) {
        final String query = "DELETE FROM users WHERE ID = ? ";
        return getJdbcTemplate().update(query, id) == 1;
    }

    /**
     * @see UserDAO#remove(am.aca.orgflix.entity.User)
     */
    @Override
    public boolean remove(User user) {
        return remove(user.getId());
    }
}