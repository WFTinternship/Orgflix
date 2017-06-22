package am.aca.dao.jdbc.impljdbc;

import am.aca.dao.*;
import am.aca.dao.jdbc.BaseDAO;
import am.aca.dao.jdbc.CastDAO;
import am.aca.entity.Cast;
import am.aca.entity.Film;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * Created by David on 5/28/2017
 */
@Repository
public class JdbcCastDAO extends BaseDAO implements CastDAO {

    @Autowired
    public JdbcCastDAO(DataSource dataSource) {
        super(JdbcCastDAO.class);
        this.setDataSource(dataSource);
    }

    // Create
    public Cast addCast(String name, boolean hasOscar) {
        Cast cast = null;
        if (!checkRequiredFields(new String[] {name}))
            throw new DaoException("Name is required!");

        KeyHolder holder = new GeneratedKeyHolder();
        final String insertQuery = "INSERT INTO casts (Actor_Name, HasOscar) VALUES( ?, ? ) ";
        getJdbcTemplate().update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setBoolean(2, hasOscar);
            return ps;
        }, holder);

        cast = new Cast(name, hasOscar);
        cast.setId(holder.getKey().intValue());

        return cast;
    }
    // support method
    @Override
    public Cast addCast(String name) {
        return addCast(name, false);
    }

    @Override
    public boolean addCastToFilm(int actorId, int filmId) {
        final String query = "INSERT INTO film_to_cast(Actor_ID,Film_ID) VALUES (? , ? ) ";
        return getJdbcTemplate().update(query, new Object[] {actorId, filmId}) == 1;
    }
    // support method
    @Override
    public boolean addCastToFilm(Cast cast, Film film) {
        return addCastToFilm(cast.getId(), film.getId());
    }


    // Read
    @Override
    public List<Cast> listCast() {
        final String query = "SELECT ID, Actor_name AS Name, HasOscar FROM casts";
         return getJdbcTemplate().query(query, new BeanPropertyRowMapper(Cast.class));
    }

    @Override
    public List<Film> listFilmsByCast(int actorId) {
        final String query = "SELECT ID, Title, Prod_Year AS prodYear,HasOscar, image_ref AS image, Director, " +
                " Rate_1star, Rate_2star, Rate_3star, Rate_4star, Rate_5star " +
                " FROM films INNER JOIN (" +
                "   SELECT film_to_cast.Film_ID AS film " +
                "   FROM casts JOIN film_to_cast " +
                "   ON casts.ID = film_to_cast.Actor_ID " +
                "   WHERE casts.ID = ? " +
                "   ORDER BY film_to_cast.Film_ID DESC " +
                ") AS sel_table "+
                " ON films.ID = sel_table.film";

        return getJdbcTemplate().query(query,
                new BeanPropertyRowMapper(Film.class),
                new Object[] {actorId});
    }

    // Update
    @Override
    public boolean editCast(Cast cast) {
        if (cast.getName() == null || cast.getName().equals(""))
            throw new DaoException();
        final String query = "UPDATE casts SET Actor_Name = ?, HasOscar = ? WHERE ID = ?";
        getJdbcTemplate().update(query, new Object[] {
            cast.getName(), cast.isHasOscar(), cast.getId()
        });
        return true;
    }
}