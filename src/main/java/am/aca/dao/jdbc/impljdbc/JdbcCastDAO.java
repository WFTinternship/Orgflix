package am.aca.dao.jdbc.impljdbc;

import am.aca.dao.DaoException;
import am.aca.dao.jdbc.BaseDAO;
import am.aca.dao.jdbc.CastDAO;
import am.aca.entity.Cast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * DAO layer for Cast entity
 */
//@Repository
public class JdbcCastDAO extends BaseDAO implements CastDAO {

    @Autowired
    public JdbcCastDAO(DataSource dataSource) {
        super(JdbcCastDAO.class);
        this.setDataSource(dataSource);
    }

    // CREATE

    /**
     * Add new actor to DB
     *
     * @param cast the actor object to be added
     * @return true if actor is added, otherwise false
     */
    @Override
    public boolean addCast(Cast cast) {
        // ensure that all required field is properly assigned
        if (!checkRequiredFields(cast.getName())) {
            throw new DaoException("Name is required");
        }
        KeyHolder holder = new GeneratedKeyHolder();
        final String query = "INSERT INTO casts (Actor_Name, HasOscar) VALUES (?, ?)";
        int result = getJdbcTemplate().update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, cast.getName());
            ps.setBoolean(2, cast.isHasOscar());
            return ps;
        }, holder);
        cast.setId(holder.getKey().intValue());
        return result == 1;
    }

    /**
     * Add an association of actor with film in DB
     *
     * @param cast   the cast with which the provided film will be associated
     * @param filmId the id of film which will be associated with provided cast
     * @return true if the new association of cast to film was successful, otherwise false
     */
    @Override
    public boolean addCastToFilm(Cast cast, int filmId) {
        final String query = "INSERT INTO film_to_cast(Actor_ID,Film_ID) VALUES (? , ? ) ";
        return getJdbcTemplate().update(query, cast.getId(), filmId) == 1;
    }

    // READ

    /**
     * List all the casts currently in DB
     *
     * @return List of all casts currently in DB
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Cast> listCast() {
        final String query = "SELECT ID, Actor_name AS Name, HasOscar FROM casts ORDER BY Name";
        return getJdbcTemplate().query(query, new BeanPropertyRowMapper(Cast.class));
    }


    // UPDATE

    /**
     * Update the fileds of the provided actor object in DB
     *
     * @param cast the actor which fields will be updated in DB
     * @return true if the update was successful, otherwise false
     */
    @Override
    public boolean editCast(Cast cast) {
        // ensure that all required field is properly assigned
        if (!checkRequiredFields(cast.getName()))
            throw new DaoException("Illegal argument");

        final String query = "UPDATE casts SET Actor_Name = ?, HasOscar = ? WHERE ID = ?";
        return getJdbcTemplate().update(query, cast.getName(), cast.isHasOscar(), cast.getId()) == 1;
    }

    // DELETE

    /**
     * Remove the provided actor from DB
     *
     * @param cast the actor which are being removed
     * @return true if the actor was removed, othrewise false
     */
    @Override
    public boolean remove(Cast cast) {
        final String query = "DELETE FROM casts WHERE ID = ?";
        return getJdbcTemplate().update(query, cast.getId()) == 1;
    }

    // Support methods
    @Override
    public boolean isStarringIn(int actorId, int filmId) {
        String query = "SELECT COUNT(*) FROM film_to_cast WHERE Film_ID = ? AND Actor_ID = ?";
        return getJdbcTemplate().queryForObject(query,
                new Object[]{filmId, actorId},
                Integer.class
        ) == 1;
    }

    @Override
    public boolean exists(Cast cast) {
        String query = "SELECT COUNT(*) FROM casts WHERE ID = ?";
        return getJdbcTemplate().queryForObject(query,
                new Object[]{cast.getId()},
                Integer.class
        ) == 1;
    }
}