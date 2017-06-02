package am.aca.dao;

import am.aca.entity.*;

import java.sql.*;
import java.util.List;

/**
 * Created by David on 5/28/2017.
 */
public class FilmDaoJdbc implements FilmDao{

    @Override
    public boolean addFilm(Film film, List<Director> directors, Connection connection)
            throws SQLException {

        final String query = "INSERT INTO films (Title,Prod_Year,HasOscar,Rate_1star,Rate_2star,Rate_3star,Rate_4star,Rate_5star) " +
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
                else return false;

            final String nextQuery = "INSERT INTO film_to_director(Director_ID,Film_ID) VALUES (? , ? ) ";
            PreparedStatement nextStatm;
            for(Director director : directors) {
                nextStatm = connection.prepareStatement(nextQuery);
                nextStatm.setInt(1, director.getId() );
                nextStatm.setInt(2, filmID );
                nextStatm.executeUpdate();
            }
            film.setId( filmID );

            return true;
        } else return false; // film insert or update statement is not successful
    }

    @Override
    public boolean editFilm(Film film, List<Director> directors, Connection connection)
            throws SQLException {

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

        int filmID;
        if( statm.executeUpdate() == 1 ){
            final String deleteQuery = "DELETE FROM film_to_director WHERE  Film_ID = ? ";
            PreparedStatement nextStatm = connection.prepareStatement(deleteQuery);
            nextStatm.setInt(1, film.getId() );
            nextStatm.executeUpdate();

            final String nextQuery = "INSERT INTO film_to_director(Director_ID,Film_ID) VALUES (? , ? ) ";
            PreparedStatement insertStatm;
            for(Director director : directors) {
                insertStatm = connection.prepareStatement(nextQuery);
                insertStatm.setInt(1, director.getId() );
                insertStatm.setInt(2, film.getId() );
                insertStatm.executeUpdate();
            }

            return true;
        } else return false; // film insert or update statement is not successful
    }

    @Override
    public Film getFilmById(int id, Connection connection) throws SQLException {
        final String getQuery = "SELECT * FROM films WHERE ID = ? ";
        PreparedStatement statm = connection.prepareStatement(getQuery);
        statm.setInt(1, id );
        ResultSet resultSet = statm.executeQuery();
        Film film = new Film();;
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
        return film;
    }


    @Override
    public boolean rateFilm(int filmId, int starType, Connection connection)
            throws SQLException {
        final String query = "UPDATE films set Rate_" + starType + "star = Rate_" +starType + "_star + 1 WHERE ID = ?";
        PreparedStatement statm = connection.prepareStatement(query);
        statm.setInt(1, filmId);
        return statm.execute();
    }

    @Override
    public boolean addGenreToFilm(Genre genre, Film film, Connection connection)
            throws SQLException {
        return false;
    }
}
