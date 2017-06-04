package am.aca.dao;

import am.aca.entity.*;
import am.aca.util.DbManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 5/28/2017
 */
public class FilmDaoJdbc implements FilmDao{

    @Override
    public boolean addFilm(Film film)
            throws SQLException {
        boolean state = false;
        Connection connection = DbManager.getConnection();
        final String query = "INSERT INTO films (Title, Prod_Year, HasOscar, Rate_1star, Rate_2star, Rate_3star, Rate_4star, Rate_5star) " +
                " VALUES( ? , ? , ?, ?, ?, ?, ?, ? ) ";

        PreparedStatement statm = connection.prepareStatement(query);
        statm.setString(1, film.getTitle() );
        statm.setInt(2, film.getProdYear() );
        statm.setBoolean(3, film.isHasOscar() );
        statm.setInt(4, film.getRate_1star() );
        statm.setInt(5, film.getRate_2star() );
        statm.setInt(6, film.getRate_3star() );
        statm.setInt(7, film.getRate_4star() );
        statm.setInt(8, film.getRate_5star() );

        int filmID;
        if( statm.executeUpdate() == 1 ){
            // getting new registered film's id
            final String selQuery = "SELECT LAST_INSERT_ID() as last_id FROM films";
            Statement selStatm = connection.createStatement();
            ResultSet resultSet = selStatm.executeQuery(selQuery);
            if ( resultSet.next() ) filmID = resultSet.getInt("last_id");
//            filmID = resultSet.getInt(1);
                else return false;

            final String nextQuery = "INSERT INTO film_to_director(Director_ID,Film_ID) VALUES (? , ? ) ";
            PreparedStatement nextStatm;
            for(Director director : film.getDirectors()) {
                nextStatm = connection.prepareStatement(nextQuery);
                nextStatm.setInt(1, director.getId() );
                nextStatm.setInt(2, filmID );
                nextStatm.executeUpdate();
            }
            film.setId( filmID );

            state = true; // film insert statement is successful
        }
        connection.close();
        return state;
    }

    @Override
    public boolean editFilm(Film film)
            throws SQLException {
        boolean state = false;
        Connection connection = DbManager.getConnection();
        final String query = "UPDATE films SET Title = ?,Prod_Year = ?,HasOscar = ?,Rate_1star = ? " +
                    ",Rate_2star = ?, Rate_3star = ?,Rate_4star = ?,Rate_5star = ? " +
                    " WHERE id = ? ";

        PreparedStatement statm = connection.prepareStatement(query);
        statm.setString(1, film.getTitle() );
        statm.setInt(2, film.getProdYear() );
        statm.setBoolean(3, film.isHasOscar() );
        statm.setInt(4, film.getRate_1star() );
        statm.setInt(5, film.getRate_2star() );
        statm.setInt(6, film.getRate_3star() );
        statm.setInt(7, film.getRate_4star() );
        statm.setInt(8, film.getRate_5star() );
        statm.setInt(9, film.getId() );

        if( statm.executeUpdate() == 1 ){
            final String deleteQuery = "DELETE FROM film_to_director WHERE  Film_ID = ? ";
            PreparedStatement nextStatm = connection.prepareStatement(deleteQuery);
            nextStatm.setInt(1, film.getId() );
            nextStatm.executeUpdate();

            final String nextQuery = "INSERT INTO film_to_director(Director_ID,Film_ID) VALUES (? , ? ) ";
            PreparedStatement insertStatm;
            for(Director director : film.getDirectors()) {
                insertStatm = connection.prepareStatement(nextQuery);
                insertStatm.setInt(1, director.getId() );
                insertStatm.setInt(2, film.getId() );
                insertStatm.executeUpdate();
            }
            state = true; // film update statement is successful
        }
        connection.close();
        return state;
    }

    @Override
    public Film getFilmById(int id) throws SQLException {
        Connection connection = DbManager.getConnection();
        final String getQuery = "SELECT * FROM films WHERE ID = ? ";
        PreparedStatement statm = connection.prepareStatement(getQuery);
        statm.setInt(1, id );
        ResultSet resultSet = statm.executeQuery();
        Film film = new Film();
        while( resultSet.next() ){
            film.setId(id);
            film.setTitle( resultSet.getString("Title") );
            film.setHasOscar( resultSet.getBoolean("HasOscar") );
            film.setProdYear( resultSet.getInt("Prod_Year") );
            film.setRate_1star( resultSet.getInt("Rate_1star") );
            film.setRate_2star( resultSet.getInt("Rate_2star") );
            film.setRate_3star( resultSet.getInt("Rate_3star") );
            film.setRate_4star( resultSet.getInt("Rate_4star") );
            film.setRate_5star( resultSet.getInt("Rate_5star") );
        }
        film.setId(id);

        final String directorQuery = "select * from film_to_director" +
                " LEFT JOIN directors on film_to_director.Director_ID = directors.ID\n" +
                " where Film_ID = ?";
        PreparedStatement dirStatm = connection.prepareStatement(directorQuery);
        dirStatm.setInt(1, id );
        ResultSet directorsSet = dirStatm.executeQuery();
        List<Director> directorList = new ArrayList<>();
        while( directorsSet.next() ){
            Director director = new Director();
            director.setId( directorsSet.getInt("Director_ID") );
            director.setName( directorsSet.getString("Director_Name") );
            director.setHasOscar( directorsSet.getBoolean("HasOscar") );
            directorList.add(director);
        }
        film.setDirectors(directorList);
        connection.close();
        return film;
    }


    @Override
    public boolean rateFilm(int filmId, int starType)
            throws SQLException {
        Connection connection = DbManager.getConnection();
        final String query = "UPDATE films set Rate_" + starType + "star = Rate_" +starType + "_star + 1 WHERE ID = ?";
        PreparedStatement statm = connection.prepareStatement(query);
        statm.setInt(1, filmId);

        boolean state = statm.execute();
        connection.close();
        return state;
    }

    @Override
    public boolean addGenreToFilm(Genre genre, Film film)
            throws SQLException {
        Connection connection = DbManager.getConnection();
        connection.close();
        return false;
    }
}
