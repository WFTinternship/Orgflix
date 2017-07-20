package am.aca.orgflix.dao.impljdbc;

import am.aca.orgflix.dao.CastDAO;
import am.aca.orgflix.dao.impljdbc.mapper.CastRowMapper;
import am.aca.orgflix.entity.Cast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
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
public class JdbcCastDAO extends NamedParameterJdbcDaoSupport implements CastDAO {

    @Autowired
    public JdbcCastDAO(DataSource dataSource) {
        setDataSource(dataSource);
    }

    // CREATE

    /**
     * @see CastDAO#add(am.aca.orgflix.entity.Cast)
     */
    @Override
    public boolean add(Cast cast) {
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
     * @see CastDAO#addToFilm(am.aca.orgflix.entity.Cast, int)
     */
    @Override
    public boolean addToFilm(Cast cast, int filmId) {
        final String query = "INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES (? , ? ) ";
        return getJdbcTemplate().update(query, cast.getId(), filmId) == 1;
    }

    // READ

    /**
     * @see am.aca.orgflix.dao.CastDAO#getAll()
     */
    @Override
    public List<Cast> getAll() {
        final String query = "SELECT ID, ACTOR_NAME, HAS_OSCAR FROM CASTS ORDER BY ACTOR_NAME";
        return getJdbcTemplate().query(query, new CastRowMapper());
    }

    /**
     * @see CastDAO#getByFilm(int)
     */
    @Override
    public List<Cast> getByFilm(int filmId) {
        final String query = "SELECT CASTS.ID, CASTS.ACTOR_NAME, CASTS.HAS_OSCAR " +
                " FROM CASTS " +
                " INNER JOIN (" +
                "    SELECT ACTOR_ID" +
                "    FROM FILM_TO_CAST" +
                "    WHERE FILM_ID = ? " +
                "    ) AS CAST_ID " +
                "  ON CASTS.ID = CAST_ID.ACTOR_ID";
        return getJdbcTemplate().query(query, new Object[]{filmId}, new CastRowMapper());
    }

    /**
     * @see CastDAO#getById(int)
     */
    public Cast getById(int castId) {
        final String query = "SELECT * FROM CASTS WHERE ID = ?";
        return getJdbcTemplate().queryForObject(query, new Object[]{castId}, new CastRowMapper());
    }

    // UPDATE

    /**
     * @see CastDAO#edit(am.aca.orgflix.entity.Cast)
     */
    @Override
    public boolean edit(Cast cast) {
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
     * @see CastDAO#isRelatedToFilm(int, int)
     */
    @Override
    public boolean isRelatedToFilm(int actorId, int filmId) {
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

    /**
     * @see CastDAO#exists(String)
     */
    @Override
    public boolean exists(String castName) {
        String query = "SELECT COUNT(*) FROM CASTS WHERE ACTOR_NAME = ?";
        return getJdbcTemplate().queryForObject(query,
                new Object[]{castName},
                Integer.class
        ) == 1;
    }
}