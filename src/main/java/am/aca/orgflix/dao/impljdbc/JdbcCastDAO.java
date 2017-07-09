package am.aca.orgflix.dao.impljdbc;

import am.aca.orgflix.dao.BaseDAO;
import am.aca.orgflix.dao.CastDAO;
import am.aca.orgflix.dao.DaoException;
import am.aca.orgflix.dao.impljdbc.mapper.CastRowMapper;
import am.aca.orgflix.entity.Cast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * DAO layer for Cast entity
 */
@Component
public class JdbcCastDAO extends BaseDAO implements CastDAO {

    @Autowired
    public JdbcCastDAO(DataSource dataSource) {
        super(JdbcCastDAO.class);
        this.setDataSource(dataSource);
    }

    // CREATE

    /**
     * @see CastDAO#addCast(am.aca.orgflix.entity.Cast)
     */
    @Override
    public boolean addCast(Cast cast) {
        // ensure that all required field is properly assigned
        if (!checkRequiredFields(cast.getName())) {
            throw new DaoException("Name is required");
        }
        KeyHolder holder = new GeneratedKeyHolder();
        final String query = "INSERT INTO CASTS (ACTOR_NAME, HAS_OSCAR) VALUES (?, ?)";
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
     * @see CastDAO#addCastToFilm(am.aca.orgflix.entity.Cast, int)
     */
    @Override
    public boolean addCastToFilm(Cast cast, int filmId) {
        final String query = "INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES (? , ? ) ";
        return getJdbcTemplate().update(query, cast.getId(), filmId) == 1;
    }

    // READ

    /**
     * @see am.aca.orgflix.dao.CastDAO#listCast()
     */
    @Override
    public List<Cast> listCast() {
        final String query = "SELECT ID, ACTOR_NAME, HAS_OSCAR FROM CASTS ORDER BY ACTOR_NAME";
        return getJdbcTemplate().query(query, new CastRowMapper());
    }

    /**
     * @see CastDAO#getCastsByFilm(int)
     */
    @Override
    public List<Cast> getCastsByFilm(int filmId) {
        final String query = "SELECT CASTS.ID, CASTS.ACTOR_NAME, CASTS.HAS_OSCAR " +
                "FROM CASTS" +
                " INNER JOIN (" +
                "    SELECT ACTOR_ID" +
                "    FROM FILM_TO_CAST" +
                "    WHERE FILM_ID = ? " +
                "    ) AS CAST_ID " +
                "  ON CASTS.ID = CAST_ID.ACTOR_ID";
        return getJdbcTemplate().query(query, new Object[]{filmId}, new CastRowMapper());
    }


    // UPDATE

    /**
     * @see CastDAO#editCast(am.aca.orgflix.entity.Cast)
     */
    @Override
    public boolean editCast(Cast cast) {
        // ensure that all required field is properly assigned
        if (!checkRequiredFields(cast.getName()))
            throw new DaoException("Illegal argument");

        final String query = "UPDATE CASTS SET ACTOR_NAME = ?, HAS_OSCAR = ? WHERE ID = ?";
        return getJdbcTemplate().update(query, cast.getName(), cast.isHasOscar(), cast.getId()) == 1;
    }

    // DELETE

    /**
     * @see CastDAO#remove(am.aca.orgflix.entity.Cast)
     */
    @Override
    public boolean remove(Cast cast) {
        final String query = "DELETE FROM CASTS WHERE ID = ?";
        return getJdbcTemplate().update(query, cast.getId()) == 1;
    }

    // Support methods

    /**
     * @see CastDAO#isStarringIn(int, int)
     */
    @Override
    public boolean isStarringIn(int actorId, int filmId) {
        String query = "SELECT COUNT(*) FROM FILM_TO_CAST WHERE FILM_ID = ? AND ACTOR_ID = ?";
        return getJdbcTemplate().queryForObject(query,
                new Object[]{filmId, actorId},
                Integer.class
        ) == 1;
    }

    /**
     * @see CastDAO#exists(am.aca.orgflix.entity.Cast)
     */
    @Override
    public boolean exists(Cast cast) {
        String query = "SELECT COUNT(*) FROM CASTS WHERE ID = ?";
        return getJdbcTemplate().queryForObject(query,
                new Object[]{cast.getId()},
                Integer.class
        ) == 1;
    }
}