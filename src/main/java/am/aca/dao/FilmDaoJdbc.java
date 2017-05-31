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
    public int rateFilm(Film film, int starType, boolean isAdd, Connection connection)
            throws SQLException {
        return 0;
    }

    @Override
    public boolean addGenreToFilm(Genre genre, Film film, Connection connection)
            throws SQLException {
        return false;
    }
}
