package am.aca.dao.jdbc.impljdbc;

import am.aca.dao.*;
import am.aca.dao.jdbc.CastDao;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.util.ConnType;
<<<<<<< Updated upstream:src/main/java/am/aca/dao/impljdbc/CastDaoJdbc.java
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
=======
>>>>>>> Stashed changes:src/main/java/am/aca/dao/jdbc/impljdbc/CastDaoJdbc.java

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * Created by David on 5/28/2017
 */
public class CastDaoJdbc extends DaoJdbc implements CastDao {

    private JdbcTemplate jdbcTemplate;

    public CastDaoJdbc(){
        super();
    }

    public CastDaoJdbc(ConnType connType){
        super(connType);
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public Cast addCast(String name, boolean hasOscar) {
        if (name == null || name.equalsIgnoreCase(""))
            throw new DaoException();

        KeyHolder holder = new GeneratedKeyHolder();
        final String insertQuery = "INSERT INTO casts (Actor_Name, HasOscar) VALUES( ?, ? ) ";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setBoolean(2, hasOscar);
            return ps;
        }, holder);

        Cast cast = new Cast();
        cast.setName(name);
        cast.setHasOscar(hasOscar);
        cast.setId(holder.getKey().intValue());

        return cast;

    }

    @Override
    public Cast addCast(String name) {
        return addCast(name, false);
    }

    @Override
    public boolean addCastToFilm(Cast cast, Film film) {
        return addCastToFilm(cast.getId(), film.getId());
    }


    @Override
    public boolean addCastToFilm(int actorId, int filmId) {
        final String query = "INSERT INTO film_to_cast(Actor_ID,Film_ID) VALUES (? , ? ) ";
        this.jdbcTemplate.update(query, new Object[] {
                actorId, filmId
        });
        return true; /////////////////TO BE UPDATED
    }


    @Override
    public List listCast() {
        final String query = "SELECT * FROM casts";
         return this.jdbcTemplate.queryForList(query);
    }




    @Override
    public List listFilmsIdByCast(int actorId) {
        final String query = "SELECT casts.ID AS Id, film_to_cast.Film_ID AS film " +
                " FROM casts JOIN film_to_cast " +
                " ON casts.ID = film_to_cast.Actor_ID " +
                " WHERE casts.ID = ? " +
                " ORDER BY film_to_cast.Film_ID DESC ";

        return this.jdbcTemplate.queryForList(query, new Object[] {actorId});

    }


    @Override
    public boolean editCast(Cast cast) {
        if (cast.getName() == null || cast.getName().equals(""))
            throw new DaoException();
        final String query = "UPDATE casts SET Actor_Name = ?, HasOscar = ? WHERE ID = ?";
        jdbcTemplate.update(query, new Object[] {
            cast.getName(), cast.isHasOscar(), cast.getId()
        });
        return true;
    }
}
