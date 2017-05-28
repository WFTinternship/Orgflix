package am.aca.DAO;

import am.aca.main.Film;
import am.aca.main.Genre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by David on 5/28/2017.
 */
public class FilmDaoJdbc implements FilmDao{

    private Connection connection;

    public FilmDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean addFilm(Film film, boolean isEdit) throws SQLException {
        final String query;
        if(isEdit)
            query = "INSERT INTO films (Title,Prod_Year,HasOscar,Rate_1star,Rate_2star,Rate_3star,Rate_4star,Rate_5star) " +
                    " VALUES( ? , ? , ?, ?, ?, ?, ?, ? ) ";
        else
            query = "UPDATE films SET Title = ?,Prod_Year = ?,HasOscar = ?,Rate_1star = ? " +
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
        if(isEdit) statm.setInt(9, film.getId() );

        return statm.executeUpdate() == 1;

    }

    @Override
    public int rateFilm(Film film, int starType, boolean isAdd) throws SQLException {
        return 0;
    }

    @Override
    public boolean addGenre(Genre genre, Film film) throws SQLException {
        return false;
    }
}
