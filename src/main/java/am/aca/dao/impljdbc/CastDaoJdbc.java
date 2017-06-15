package am.aca.dao.impljdbc;

import am.aca.dao.*;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.util.ConnType;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 5/28/2017
 */
public class CastDaoJdbc extends DaoJdbc implements CastDao {

    private static final Logger LOGGER = Logger.getLogger(CastDao.class);

    public CastDaoJdbc(){
        super();
    }

    public CastDaoJdbc(ConnType connType){
        super(connType);
    }

    public Cast addCast(String name, boolean hasOscar) {

        final String query = "INSERT INTO casts (Actor_Name,HasOscar) VALUES( ?, ? ) ";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int id = -1;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setBoolean(2, hasOscar);

            if (statement.executeUpdate() == 1) {
                resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) id = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.warn(e.toString());
            throw new DaoException(e.toString());
        } finally {
            dataSource.closeConnections(new Object[]{resultSet, statement, connection});
        }

        Cast cast = new Cast();
        cast.setName(name);
        cast.setHasOscar(hasOscar);
        cast.setId(id);

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
        boolean state;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, actorId);
            statement.setInt(2, filmId);
            state = statement.execute();
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        } finally {
            dataSource.closeConnections(new Object[]{statement, connection});
        }
        return state;
    }

    @Override
    public List<Cast> listCast() {
        List<Cast> listCast = new ArrayList<>();
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            resultSet = connection.createStatement().executeQuery("SELECT * FROM casts");

            while (resultSet.next()) {
                Cast cast = new Cast();
                cast.setId(resultSet.getInt("ID"));
                cast.setName(resultSet.getString("Actor_Name"));
                cast.setHasOscar(resultSet.getBoolean("HasOscar"));
                listCast.add(cast);
            }
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        } finally {
            dataSource.closeConnections(new Object[]{resultSet, connection});
        }
        return listCast;
    }

    @Override
    public List<Integer> listFilmsIdByCast(int actorId) {

        List<Integer> filmsByDirector = new ArrayList<>();

        final String query = "SELECT casts.ID AS Id, film_to_cast.Film_ID AS film " +
                " FROM casts JOIN film_to_cast " +
                " ON casts.ID = film_to_cast.Actor_ID " +
                " WHERE casts.ID = ? " +
                " ORDER BY film_to_cast.Film_ID DESC ";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, actorId);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                filmsByDirector.add(resultSet.getInt("Film"));
            }
        } catch (SQLException e) {
            LOGGER.warn(e.toString());
            throw new DaoException(e.toString());
        } finally {
            dataSource.closeConnections(new Object[]{resultSet, statement, connection});
        }
        return filmsByDirector;
    }

    @Override
    public boolean editCast(Cast cast) {
        boolean state;
        Connection connection = null;
        PreparedStatement statement = null;
        final String query = "UPDATE casts SET Actor_Name = ?, HasOscar = ? WHERE ID = ?";
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, cast.getName());
            statement.setBoolean(2, cast.isHasOscar());
            statement.setInt(3, cast.getId());
            state = statement.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.warn(e.toString());
            throw new DaoException(e.toString());
        } finally {
            dataSource.closeConnections(new Object[]{statement, connection});
        }
        return state;
    }
}
